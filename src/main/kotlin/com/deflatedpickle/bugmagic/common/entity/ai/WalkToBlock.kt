package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos

class WalkToBlock(private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        if (!entityIn.dataManager.get(ItemCollector.dataItemStack).isEmpty
                && entityIn.dataManager.get(ItemCollector.dataInventoryPosition) != BlockPos.ORIGIN) {
            return true
        }
        return false
    }

    override fun updateTask() {
        val path = this.entityIn.navigator.getPathToPos(entityIn.dataManager.get(ItemCollector.dataInventoryPosition))

        if (path != null) {
            this.entityIn.navigator.setPath(path, 0.2)
        }
    }
}