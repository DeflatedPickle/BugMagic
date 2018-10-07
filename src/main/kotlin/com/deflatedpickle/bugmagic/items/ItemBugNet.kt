package com.deflatedpickle.bugmagic.items

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemBugNet(stackSize: Int, private val bugLimit: Int) : Item() {
    init {
        setMaxStackSize(stackSize)
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (entityLiving is EntityPlayer) {
            if (!worldIn.isRemote) {
                if (!stack.hasTagCompound()) {
                    stack.tagCompound = NBTTagCompound()
                }
                val compound = stack.tagCompound!!

                if (entityLiving.isSneaking) {
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
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return true
    }

    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        return if (stack.hasTagCompound()) {
            (bugLimit - stack.tagCompound!!.getInteger("bugs").toDouble()) / bugLimit
        }
        else {
            1.0
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return 32
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }
}