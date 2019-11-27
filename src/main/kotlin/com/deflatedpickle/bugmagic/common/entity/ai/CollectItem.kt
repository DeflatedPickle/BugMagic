package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack

class CollectItem(private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        return !entityIn.navigator.noPath() && entityIn.dataManager.get(ItemCollector.dataItemStack) == ItemStack.EMPTY
    }

    override fun updateTask() {
        val item = entityIn.world.findNearestEntityWithinAABB(EntityItem::class.java,
                entityIn.entityBoundingBox, this.entityIn)

        if (item != null && item is EntityItem) {
            entityIn.dataManager.set(ItemCollector.dataItemStack, item.item)
            item.setDead()
        }
    }
}