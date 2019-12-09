/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class CollectItem(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val stack = entityIn.dataManager.get(ItemCollector.dataItemStack)
        return !entityIn.navigator.noPath() &&
                (stack == ItemStack.EMPTY || stack.item == Items.AIR || stack.item == Blocks.AIR)
    }

    override fun updateTask() {
        BugMagic.logger.debug("$entityIn picked up $findItem")
        val item = findItem.entity

        if (item != null) {
            if (entityIn.entityBoundingBox.grow(0.8).intersects(item.entityBoundingBox)) {
                entityIn.dataManager.set(ItemCollector.dataItemStack, item.item.splitStack(1))

                if (item.item.count < 0) {
                    item.setDead()
                }
            }
        }
    }
}
