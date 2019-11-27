package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos

class WalkToItem(private val entityIn: EntityLiving) : EntityAIBase() {
    var entity: Entity? = null

    override fun shouldExecute(): Boolean {
        if (entityIn.dataManager.get(ItemCollector.dataItemStack).isEmpty
                && entityIn.dataManager.get(ItemCollector.dataInventoryPosition) != BlockPos.ORIGIN) {
            return true
        }
        return false
    }

    override fun updateTask() {
        entity = this.entityIn.world.findNearestEntityWithinAABB(EntityItem::class.java,
                AxisAlignedBB(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)
                        .add(0.5, 0.0, 0.5))
                        .grow(5.0),
                this.entityIn)

        if (entity != null) {
            val path = this.entityIn.navigator.getPathToEntityLiving(entity!!)

            if (path != null) {
                this.entityIn.navigator.setPath(path, 0.2)
            }
        }
    }
}