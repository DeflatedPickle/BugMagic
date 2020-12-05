/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.datasync.DataParameter
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

/**
 * An AI task to bring an [Item] found with [AIFindItem] to an [ItemStackHandler]
 *
 * @author DeflatedPickle
 * @param findItem The task to give us a found item
 * @param entityIn The entity this task is applied to
 */
// TODO: Make this not dependent on ItemCollector
class AIDeliverToInventory(
    private val findItem: AIFindItem,
    private val entityIn: EntityLiving,
    private val itemParameter: DataParameter<ItemStack>,
    private val inventoryParameter: DataParameter<BlockPos>
) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val blockPos = this.entityIn.dataManager.get(this.inventoryParameter)
        return blockPos != BlockPos.ORIGIN && entityIn.getDistanceSq(
                blockPos.x.toDouble() + 0.5,
                blockPos.y.toDouble(),
                blockPos.z.toDouble() + 0.5
        ) < 1.0
    }

    override fun updateTask() {
        val inventoryBlockPos = this.entityIn.dataManager.get(this.inventoryParameter)
        val itemStack = this.entityIn.dataManager.get(this.itemParameter)

        val tileEntity = this.entityIn.world.getTileEntity(inventoryBlockPos)

        if (tileEntity != null) {
            val itemHandler = tileEntity.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null
            )

            if (itemHandler is IItemHandlerModifiable) {
                entityIn.dataManager.set(
                    this.itemParameter,
                    ItemHandlerHelper.insertItemStacked(
                        itemHandler, itemStack, false
                    )
                )
            }
        }
    }
}
