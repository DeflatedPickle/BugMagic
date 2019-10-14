package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

class GenericWand(name: String) : Generic(name, CreativeTabs.TOOLS) {
    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (entityLiving is EntityPlayer) {
            if (entityLiving.hasCapability(SpellLearner.Provider.CAPABILITY!!, null)) {
                entityLiving.getCapability(SpellLearner.Provider.CAPABILITY!!, null)!!.also {
                }
            }
        }

        return stack
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
}