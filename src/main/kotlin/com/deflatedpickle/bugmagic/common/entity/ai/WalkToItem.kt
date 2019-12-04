package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos

class WalkToItem(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        if (entityIn is EntityTameable) {
            if (entityIn.owner == null) {
                return false
            }
        }

        if (entityIn.dataManager.get(ItemCollector.dataItemStack).isEmpty
                && entityIn.dataManager.get(ItemCollector.dataInventoryPosition) != BlockPos.ORIGIN
                && entityIn.world.getTileEntity(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)) != null) {
            return true
        }
        return false
    }

    override fun updateTask() {
        val item = findItem.entity

        if (item != null) {
            val path = this.entityIn.navigator.getPathToEntityLiving(item)

            if (path != null) {
                this.entityIn.navigator.setPath(path, entityIn.aiMoveSpeed.toDouble())
            }
        }
    }
}