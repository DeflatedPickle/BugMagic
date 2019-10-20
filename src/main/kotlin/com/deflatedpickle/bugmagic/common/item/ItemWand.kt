package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.client.Proxy as ClientProxy
import com.deflatedpickle.bugmagic.server.Proxy as ServerProxy
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
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

class ItemWand(name: String) : Generic(name, CreativeTabs.TOOLS) {
    companion object {
        const val SPELL_INDEX = "spell_index"
    }

    var hasBeenScrolled = false

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (!worldIn.isRemote) {
            if (entityLiving is EntityPlayer) {
                val bugEssence = BugEssence.isCapable(entityLiving)
                val spellLearner = SpellLearner.isCapable(entityLiving)
                val spellCaster = SpellCaster.isCapable(entityLiving.heldItemMainhand)

                if (bugEssence != null && spellLearner != null && spellCaster != null) {
                    val manaCost = spellLearner.spellList[spellLearner.currentIndex].manaCost

                    if (bugEssence.current - manaCost >= 0) {
                        bugEssence.current -= manaCost
                        BugMagic.CHANNEL.sendTo(MessageBugEssence(bugEssence.max, bugEssence.current), entityLiving as EntityPlayerMP?)

                        spellLearner.spellList[spellLearner.currentIndex].cast()

                        spellCaster.isCasting = false
                    }
                }
            }
        }

        return stack
    }

    // This happens on both sides
    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
        if (entityLiving is EntityPlayer) {
            val spellCaster = SpellCaster.isCapable(entityLiving.heldItemMainhand)

            if (spellCaster != null) {
                spellCaster.isCasting = false
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

            if (isSelected && entityIn is EntityPlayer && entityIn.isSneaking) {
                if (entityIn.hasCapability(SpellLearner.Provider.CAPABILITY!!, null)) {
                    entityIn.getCapability(SpellLearner.Provider.CAPABILITY!!, null)!!.also {
                        // TODO: Sync the selected spell with the server
                        if (it.spellList.size > 0) {
                            with(hasBeenScrolled) {
                                if (this) {
                                    with(stack.tagCompound!!.getInteger(SPELL_INDEX)) {
                                        when (Mouse.getEventDWheel()) {
                                            // Forward
                                            -120 -> {
                                                stack.tagCompound!!.setInteger(SPELL_INDEX,
                                                        if (this < it.spellList.lastIndex) this + 1 else 0)
                                            }
                                            // Backwards
                                            120 -> {
                                                stack.tagCompound!!.setInteger(SPELL_INDEX,
                                                        if (this > 0) this - 1 else it.spellList.lastIndex)
                                            }
                                        }
                                    }

                                    Minecraft.getMinecraft().ingameGUI.setOverlayMessage(TextComponentString("" +
                                            // The last spell
                                            "${TextFormatting.GRAY}${it.spellList[
                                                    if (stack.tagCompound!!.getInteger(SPELL_INDEX) - 1 < 0)
                                                        it.spellList.lastIndex
                                                    else
                                                        stack.tagCompound!!.getInteger(SPELL_INDEX) - 1].name} < "

                                            // This spell
                                            + "${TextFormatting.WHITE}${it.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)].name}"

                                            // The next spell
                                            + "${TextFormatting.GRAY} > ${it.spellList[
                                            if (stack.tagCompound!!.getInteger(SPELL_INDEX) + 1 > it.spellList.lastIndex)
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
                }
            }
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        if (stack.hasTagCompound() && worldIn!!.isRemote) {
            val spellLearner = SpellLearner.isCapable(BugMagic.proxy!!.getPlayer()!!)

            if (spellLearner != null) {
                tooltip.add("Spell: ${spellLearner.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)].name}")
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

                if (spellLearner != null) {
                    spellLearner.spellList[spellLearner.currentIndex].castingTime
                }
                else {
                    0
                }
            }
            is ServerProxy -> {
                if (spellCaster != null && spellCaster.owner != null) {
                    val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(spellCaster.owner!!)
                    val spellLearner = SpellLearner.isCapable(player)

                    if (spellLearner != null) {
                        spellLearner.spellList[spellLearner.currentIndex].castingTime
                    }
                    else {
                        0
                    }
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