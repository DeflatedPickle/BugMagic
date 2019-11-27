package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.CapabilityItemHandler

class DeliverToInventory(private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val blockPos = this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition)
        return blockPos != BlockPos.ORIGIN && entityIn.getDistanceSq(
                blockPos.x.toDouble() + 0.5,
                blockPos.y.toDouble(),
                blockPos.z.toDouble() + 0.5
        ) < 1.0
    }

    override fun updateTask() {
        val tileEntity = this.entityIn.world.getTileEntity(this.entityIn.dataManager.get(ItemCollector.dataInventoryPosition))
        if (tileEntity != null) {
            val itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
            if (itemHandler != null) {
                for (i in 0..itemHandler.slots) {
                    if (itemHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                        itemHandler.insertItem(i, this.entityIn.dataManager.get(ItemCollector.dataItemStack), false)
                        this.entityIn.dataManager.set(ItemCollector.dataItemStack, ItemStack.EMPTY)
                        break
                    }
                }
            }
        }
    }
}