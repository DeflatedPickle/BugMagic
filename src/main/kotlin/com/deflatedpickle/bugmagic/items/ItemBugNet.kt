package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemBugNet(name: String, stackSize: Int, creativeTab: CreativeTabs, val bugLimit: Int) : ItemBase(name, stackSize, creativeTab) {
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (!playerIn.world.isRemote) {
            val stack = playerIn.getHeldItem(handIn)

            if (!stack.hasTagCompound()) {
                stack.tagCompound = NBTTagCompound()
            }
            val compound = stack.tagCompound!!

            if (playerIn.isSneaking) {
                compound.setInteger("bugs", 0)
            }
            else {
                // TODO: Change this to catch a small portion of the amount of bugs in the biome, based on biome type and size
                val caughtAmount = (0..3).shuffled().last()

                val bugAmount = compound.getInteger("bugs")

                if (bugAmount < bugLimit) {
                    if (bugAmount + caughtAmount < bugLimit) {
                        compound.setInteger("bugs", caughtAmount + bugAmount)
                    }
                    else {
                        compound.setInteger("bugs", bugLimit)
                    }
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun showDurabilityBar(stack: ItemStack?): Boolean {
        return true
    }

    override fun getDurabilityForDisplay(stack: ItemStack?): Double {
        return if (stack!!.hasTagCompound()) {
            (bugLimit - stack.tagCompound!!.getInteger("bugs").toDouble()) / bugLimit
        }
        else {
            1.0
        }
    }
}