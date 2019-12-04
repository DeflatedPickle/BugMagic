/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.item.ItemStack

class CollectItem(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        return !entityIn.navigator.noPath() && entityIn.dataManager.get(ItemCollector.dataItemStack) == ItemStack.EMPTY
    }

    override fun updateTask() {
        val item = findItem.entity

        if (item != null) {
            if (entityIn.entityBoundingBox.grow(0.4).intersects(item.entityBoundingBox)) {
                entityIn.dataManager.set(ItemCollector.dataItemStack, item.item.splitStack(1))

                if (item.item.count < 0) {
                    item.setDead()
                }
            }
        }
    }
}
