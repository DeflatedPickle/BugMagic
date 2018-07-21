package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.util.BugUtil
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemWand(name: String, stackSize: Int, creativeTab: CreativeTabs) : ItemBase(name, stackSize, creativeTab) {
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
            else {
                // TODO: Check the kind of wizard before casting the spell
                BugUtil.useBugPower(playerIn, 5)
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        return 36000
    }
}