/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

/**
 * An AI task to collect an item found by [AIFindItem]
 *
 * @author DeflatedPickle
 * @param findItem The [AIFindItem] task linked with the given [EntityLiving]
 * @param entityIn The [EntityLiving] this task is added to
 */
class AICollectItem(
    private val findItem: AIFindItem,
    private val entityIn: EntityLiving
) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val stack = entityIn.dataManager.get(ItemCollectorEntity.dataItemStack)
        return findItem.entityItem != null &&
                (stack == ItemStack.EMPTY || stack.item == Items.AIR || stack.item == Blocks.AIR)
    }

    override fun updateTask() {
        val entityItem = findItem.entityItem

        if (entityItem != null) {
            if (entityIn.entityBoundingBox.grow(0.8).intersects(entityItem.entityBoundingBox.grow(0.4))) {
                entityIn.dataManager.set(ItemCollectorEntity.dataItemStack, entityItem.item.splitStack(1))
                BugMagic.logger.debug("$entityIn picked up $findItem")

                if (entityItem.item.count <= 0) {
                    entityItem.setDead()
                    this.findItem.entityItem = null
                }
            }
        }
    }
}
