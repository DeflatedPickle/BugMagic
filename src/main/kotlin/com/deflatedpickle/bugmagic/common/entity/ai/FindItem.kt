package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB

class FindItem(private val entityIn: EntityLiving) : EntityAIBase() {
    var entity: EntityItem? = null

    override fun shouldExecute(): Boolean {
        return entity == null || entity!!.onGround || entity!!.item.count == 1
    }

    override fun updateTask() {
        entity = this.entityIn.world.findNearestEntityWithinAABB(EntityItem::class.java,
                AxisAlignedBB(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)
                        .add(0.5, 0.0, 0.5))
                        .grow(5.0),
                this.entityIn) as EntityItem?
    }
}