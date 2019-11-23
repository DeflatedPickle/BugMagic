package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.client.Proxy as ClientProxy
import com.deflatedpickle.bugmagic.server.Proxy as ServerProxy
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.dedicated.DedicatedPlayerList
import net.minecraft.server.management.PlayerList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.common.FMLCommonHandler
import org.lwjgl.input.Mouse
import java.util.concurrent.ThreadLocalRandom

class Wand(name: String) : Generic(name, CreativeTabs.TOOLS) {
    companion object {
        const val SPELL_INDEX = "spell_index"
    }

    var hasBeenScrolled = false

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        if (entityLiving is EntityPlayer && !entityLiving.world.isRemote) {
            val spellLearner = SpellLearner.isCapable(entityLiving)
            val spellCaster = SpellCaster.isCapable(entityLiving.heldItemMainhand)

            if (spellLearner != null && spellCaster != null) {
                if (spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
                    if (spellCaster.castSpellMap[spellLearner.currentSpell]!! > 0) {
                        spellLearner.currentSpell.uncast()
                        spellCaster.castSpellMap[spellLearner.currentSpell] = spellCaster.castSpellMap[spellLearner.currentSpell]!! - 1
                    }
                    else {
                        spellLearner.currentSpell.uncast()
                        spellCaster.castSpellMap.remove(spellLearner.currentSpell)
                    }
                }

                return false
            }
        }
        return true
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (entityLiving is EntityPlayer) {
            val bugEssence = BugEssence.isCapable(entityLiving)
            val spellLearner = SpellLearner.isCapable(entityLiving)
            val spellCaster = SpellCaster.isCapable(entityLiving.heldItemMainhand)

            if (bugEssence != null && spellLearner != null && spellCaster != null) {
                val manaCost = spellLearner.currentSpell.manaCost

                if (bugEssence.current - manaCost >= 0) {
                    if (!worldIn.isRemote) {
                        bugEssence.current -= manaCost
                        BugMagic.CHANNEL.sendTo(MessageBugEssence(bugEssence.max, bugEssence.current), entityLiving as EntityPlayerMP?)

                        if (!spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
                            spellLearner.currentSpell.cast()
                            spellCaster.castSpellMap[spellLearner.currentSpell] = 0
                        }
                        else {
                            if (spellCaster.castSpellMap[spellLearner.currentSpell] != spellLearner.currentSpell.maxCount) {
                                spellLearner.currentSpell.cast()
                                spellCaster.castSpellMap[spellLearner.currentSpell] = spellCaster.castSpellMap[spellLearner.currentSpell]!! + 1
                            }
                        }

                        entityLiving.cooldownTracker.setCooldown(this, spellLearner.currentSpell.maxCooldown)

                        spellCaster.isCasting = false

                        BugMagic.CHANNEL.sendTo(MessageSpellCaster(spellCaster.isCasting, spellCaster.castingCurrent), entityLiving as EntityPlayerMP?)
                    }
                    else {
                        if (spellLearner.currentSpell.finishingParticle != null) {
                            entityLiving.world.spawnParticle(spellLearner.currentSpell.finishingParticle!!,
                                    entityLiving.posX,
                                    entityLiving.posY + entityLiving.eyeHeight,
                                    entityLiving.posZ,
                                    0.0, 0.0, 0.0)
                        }
                    }
                }
            }
        }

        return stack
    }

    override fun onUsingTick(stack: ItemStack, player: EntityLivingBase, count: Int) {
        if (player.world.isRemote) {
            val spellLearner = SpellLearner.isCapable(player)

            if (spellLearner != null && spellLearner.currentSpell.castingParticle != null) {
                val size = (spellLearner.currentSpell.radius + spellLearner.currentSpell.castingShapeThickness) * spellLearner.currentSpell.tier.ordinal.toDouble()

                player.world.spawnParticle(spellLearner.currentSpell.castingParticle!!,
                        player.posX + ThreadLocalRandom.current().nextDouble(-size, size),
                        player.posY + ThreadLocalRandom.current().nextDouble(-size, size),
                        player.posZ + ThreadLocalRandom.current().nextDouble(-size, size),
                        0.0, 0.0, 0.0)
            }
        }
    }

    // This happens on both sides
    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
        if (entityLiving is EntityPlayer) {
            val spellLearner = SpellLearner.isCapable(entityLiving)
            val spellCaster = SpellCaster.isCapable(entityLiving.heldItemMainhand)

            if (spellLearner != null && spellCaster != null) {
                spellCaster.isCasting = false

                if (worldIn.isRemote) {
                    if (spellLearner.currentSpell.cancelingParticle != null) {
                        entityLiving.world.spawnParticle(spellLearner.currentSpell.cancelingParticle!!,
                                entityLiving.posX,
                                entityLiving.posY,
                                entityLiving.posZ,
                                0.0, 0.0, 0.0)
                    }
                }
            }
        }
    }

    override fun onDroppedByPlayer(item: ItemStack, player: EntityPlayer): Boolean {
        val spellCaster = SpellCaster.isCapable(player.heldItemMainhand)

        if (spellCaster != null) {
            spellCaster.isCasting = false
            spellCaster.owner = null
        }

        return true
    }

    fun onMouseEvent() {
        hasBeenScrolled = true
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        // Only runs on the client
        if (worldIn.isRemote) {
            if (!stack.hasTagCompound()) {
                stack.tagCompound = NBTTagCompound()
            }

            if (isSelected && entityIn is EntityPlayer) {
                val spellLearner = SpellLearner.isCapable(entityIn)
                val spellCaster = SpellCaster.isCapable(entityIn.heldItemMainhand)

                if (entityIn.isSneaking) {
                    if (spellLearner != null) {
                        if (spellLearner.spellList.size > 0) {
                            if (hasBeenScrolled) {
                                with(stack.tagCompound!!.getInteger(SPELL_INDEX)) {
                                    when (Mouse.getEventDWheel()) {
                                        // Forward
                                        -120 -> {
                                            stack.tagCompound!!.setInteger(SPELL_INDEX,
                                                    if (this < spellLearner.spellList.lastIndex) this + 1 else 0)
                                        }
                                        // Backwards
                                        120 -> {
                                            stack.tagCompound!!.setInteger(SPELL_INDEX,
                                                    if (this > 0) this - 1 else spellLearner.spellList.lastIndex)
                                        }
                                    }
                                }

                                Minecraft.getMinecraft().ingameGUI.setOverlayMessage(TextComponentString("" +
                                        // The last spell
                                        "${TextFormatting.GRAY}${spellLearner.spellList[
                                                if (stack.tagCompound!!.getInteger(SPELL_INDEX) - 1 < 0)
                                                    spellLearner.spellList.lastIndex
                                                else
                                                    stack.tagCompound!!.getInteger(SPELL_INDEX) - 1].name} < "

                                        // This spell
                                        + "${TextFormatting.WHITE}${spellLearner.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)].name}"

                                        // The next spell
                                        + "${TextFormatting.GRAY} > ${spellLearner.spellList[
                                        if (stack.tagCompound!!.getInteger(SPELL_INDEX) + 1 > spellLearner.spellList.lastIndex)
                                            0
                                        else
                                            stack.tagCompound!!.getInteger(SPELL_INDEX) + 1].name}"
                                ), true)

                                BugMagic.CHANNEL.sendToServer(MessageSelectedSpell(stack.tagCompound!!.getInteger(SPELL_INDEX)))

                                hasBeenScrolled = false
                            }
                        }
                    }
                }
                else {
                    if (spellCaster != null && spellCaster.isCasting) {
                        spellCaster.castingCurrent++
                    }
                }
            }
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        if (stack.hasTagCompound() && worldIn!!.isRemote) {
            val spellLearner = SpellLearner.isCapable(BugMagic.proxy!!.getPlayer()!!)

            if (spellLearner != null) {
                val currentSpell = spellLearner.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)]
                tooltip.add("Spell: ${currentSpell.name} (${currentSpell.cult.colour}${currentSpell.cult}${TextFormatting.GRAY})")
            }
        }
    }

    // TODO: Fix this, I'm not completely sure this works, plus it's messy
    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        val spellCaster = SpellCaster.isCapable(stack)

        return when (BugMagic.proxy) {
            is ClientProxy -> {
                val player = Minecraft.getMinecraft().player
                val spellLearner = SpellLearner.isCapable(player)

                spellLearner?.currentSpell?.castingTime ?: 0
            }
            is ServerProxy -> {
                if (spellCaster != null && spellCaster.owner != null) {
                    val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(spellCaster.owner!!)
                    val spellLearner = SpellLearner.isCapable(player)

                    spellLearner?.currentSpell?.castingTime ?: 0
                }
                else {
                    0
                }
            }
            else -> 0
        }
    }

    override fun getItemUseAction(stack: ItemStack?): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        val spellCaster = SpellCaster.isCapable(playerIn.getHeldItem(playerIn.activeHand))

        return if (spellCaster != null) {
            spellCaster.isCasting = true
            ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
        }
        else {
            ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn))
        }
    }

    override fun isDamageable(): Boolean {
        return false
    }
}