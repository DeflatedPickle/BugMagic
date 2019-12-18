/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos

class WalkToItem(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val stack = entityIn.dataManager.get(ItemCollector.dataItemStack)
        if (stack.isEmpty &&
                entityIn.dataManager.get(ItemCollector.dataInventoryPosition) != BlockPos.ORIGIN &&
                entityIn.world.getTileEntity(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)) != null) {
            return true
        }
        return false
    }

    override fun updateTask() {
        findItem.entityItem?.let {
            val path = this.entityIn.navigator.getPathToEntityLiving(it)

            path?.let { oldTownRoad ->
                this.entityIn.navigator.setPath(oldTownRoad, entityIn.aiMoveSpeed.toDouble())
            }
        }
    }
}
