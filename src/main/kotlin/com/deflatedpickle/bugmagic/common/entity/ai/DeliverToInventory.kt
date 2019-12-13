/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemHandlerHelper

// TODO: Make this not dependent on ItemCollector
class DeliverToInventory(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val blockPos = this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition)
        return blockPos != BlockPos.ORIGIN && entityIn.getDistanceSq(
                blockPos.x.toDouble() + 0.5,
                blockPos.y.toDouble(),
                blockPos.z.toDouble() + 0.5
        ) < 1.0
    }

    override fun updateTask() {
        BugMagic.logger.debug("$entityIn delivered ${this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition)} to ${this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition)}")
        val tileEntity = this.entityIn.world.getTileEntity(this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition))

        if (tileEntity != null) {
            val itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)

            if (itemHandler is IItemHandlerModifiable) {
                val insertStack = this.entityIn.dataManager.get(ItemCollector.dataItemStack)

                entityIn.dataManager.set(ItemCollector.dataItemStack, ItemHandlerHelper.insertItem(itemHandler, insertStack, false))
            }
        }
    }
}
