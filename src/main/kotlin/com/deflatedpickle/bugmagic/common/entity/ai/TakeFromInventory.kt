/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanter
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.item.ItemSeeds
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

class TakeFromInventory(private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val blockPos = this.entityIn.dataManager.get(AutoPlanter.dataHomePosition)
        return blockPos != BlockPos.ORIGIN && this.entityIn.dataManager.get(AutoPlanter.dataSeeds) == ItemStack.EMPTY && entityIn.getDistanceSq(
                blockPos.x.toDouble() + 0.5,
                blockPos.y.toDouble(),
                blockPos.z.toDouble() + 0.5
        ) < 1.0
    }

    override fun updateTask() {
        val tileEntity = this.entityIn.world.getTileEntity(this.entityIn.dataManager.get(AutoPlanter.dataHomePosition))

        if (tileEntity != null) {
            val itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)

            if (itemHandler is IItemHandlerModifiable) {
                var slot = 0
                for (i in 0 until itemHandler.slots) {
                    val stack = itemHandler.getStackInSlot(i)
                    if (stack.item is ItemSeeds) {
                        slot = i
                        BugMagic.logger.debug("$entityIn is taking $stack from $tileEntity")
                        break
                    }
                }
                entityIn.dataManager.set(AutoPlanter.dataSeeds, itemHandler.extractItem(slot, 1, false))
            }
        }
    }
}
