package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.util.BugUtil
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import org.lwjgl.input.Mouse

class ItemWand(name: String, stackSize: Int, creativeTab: CreativeTabs) : ItemBase(name, stackSize, creativeTab) {
    var hasBeenScrolled = false

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (playerIn.isSneaking) {
            if (!playerIn.world.isRemote) {
                // TODO: Figure out a way to change spells
                val stack = playerIn.getHeldItem(handIn)

                if (!stack.hasTagCompound()) {
                    stack.tagCompound = NBTTagCompound()
                    // TODO: Save the spell as an NBT tag
                }
            }
        }
        else {
            // TODO: Check the kind of wizard before casting the spell
            if (!playerIn.world.isRemote) {
                BugUtil.useCappedBugPower(playerIn, 5)
                // SpellFirefly(playerIn).cast()
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    fun onMouseEvent() {
        hasBeenScrolled = true
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (isSelected) {
            if (entityIn is EntityPlayer) {
                if (entityIn.isSneaking) {
                    if (hasBeenScrolled) {
                        val mouseWheel = Mouse.getEventDWheel()

                        if (mouseWheel == 120) {
                            // Scrolled forward
                        }
                        else if (mouseWheel == -120) {
                            // Scrolled backwards
                        }

                        hasBeenScrolled = false
                    }
                }
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected)
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        return 36000
    }
}