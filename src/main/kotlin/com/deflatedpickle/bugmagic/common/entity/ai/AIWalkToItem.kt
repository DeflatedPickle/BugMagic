/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.BlockPos

/**
 * An AI task to walk to an [EntityItem]
 *
 * @author DeflatedPickle
 * @param findItem The [AIFindItem] attached to [entityIn]
 * @param entityIn The [EntityLiving] this task is attatched to
 */
// TODO: AIWalkToItem shouldn't be dependant on ItemCollector
class AIWalkToItem(
        private val findItem: AIFindItem,
        private val entityIn: EntityLiving
) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val stack = entityIn.dataManager.get(ItemCollectorEntity.dataItemStack)
        if (stack.isEmpty &&
                entityIn.dataManager.get(ItemCollectorEntity.dataInventoryPosition) != BlockPos.ORIGIN &&
                entityIn.world.getTileEntity(entityIn.dataManager.get(ItemCollectorEntity.dataInventoryPosition)) != null) {
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
