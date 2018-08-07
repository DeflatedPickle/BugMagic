package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.util.BugUtil
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemBugJuice(name: String, val amount: Int) : ItemBase(name, 1, ModCreativeTabs.tabGeneral) {
    override fun onItemUseFinish(stack: ItemStack?, worldIn: World?, entityLiving: EntityLivingBase?): ItemStack {
        if (entityLiving is EntityPlayer) {
            if (worldIn!!.isRemote) {
                // TODO: Check the players bug power plus the amount first
                BugUtil.giveCappedBugPower(entityLiving, amount)

                stack!!.shrink(1)
                entityLiving.inventory.addItemStackToInventory(ItemStack(Items.GLASS_BOTTLE))
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        return 32
    }

    override fun getItemUseAction(stack: ItemStack?): EnumAction {
        return EnumAction.DRINK
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer?, handIn: EnumHand?): ActionResult<ItemStack> {
        playerIn!!.activeHand = handIn!!
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }
}