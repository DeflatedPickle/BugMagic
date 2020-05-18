/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

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
        return findItem.entityItem != null &&
                (stack == ItemStack.EMPTY || stack.item == Items.AIR || stack.item == Blocks.AIR)
    }

    override fun updateTask() {
        val entityItem = findItem.entityItem

        if (entityItem != null) {
            if (entityIn.entityBoundingBox.grow(0.8).intersects(entityItem.entityBoundingBox.grow(0.4))) {
                entityIn.dataManager.set(ItemCollector.dataItemStack, entityItem.item.splitStack(1))
                BugMagic.logger.debug("$entityIn picked up $findItem")

                if (entityItem.item.count <= 0) {
                    entityItem.setDead()
                }
            }
        }
    }
}
