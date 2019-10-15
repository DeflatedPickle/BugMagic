package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import org.lwjgl.input.Mouse

class ItemWand(name: String) : Generic(name, CreativeTabs.TOOLS) {
    companion object {
        const val SPELL_INDEX = "spell_index"
    }

    var hasBeenScrolled = false

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (!worldIn.isRemote) {
            if (entityLiving is EntityPlayer) {
                if (entityLiving.hasCapability(SpellLearner.Provider.CAPABILITY!!, null)) {
                    entityLiving.getCapability(SpellLearner.Provider.CAPABILITY!!, null)!!.also {
                        it.spellList[it.currentIndex].cast()
                    }
                }
            }
        }

        return stack
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
            with(BugMagic.proxy!!.getPlayer()!!) {
                if (this.hasCapability(SpellLearner.Provider.CAPABILITY!!, null)) {
                    this.getCapability(SpellLearner.Provider.CAPABILITY!!, null).also {
                        return@with tooltip.add("Spell: ${it!!.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)].name}")
                    }
                }
                else {
                    return@with stack.tagCompound!!.getInteger(SPELL_INDEX)
                }
            }
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        // TODO: Set the use duration with each spell
        return 32
    }

    override fun getItemUseAction(stack: ItemStack?): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer?, handIn: EnumHand?): ActionResult<ItemStack> {
        playerIn!!.activeHand = handIn!!
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }

    override fun isDamageable(): Boolean {
        return false
    }
}