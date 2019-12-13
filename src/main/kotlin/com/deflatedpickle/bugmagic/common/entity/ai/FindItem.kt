/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.util.math.AxisAlignedBB

class FindItem(private val entityIn: EntityLiving) : EntityAIBase() {
    var entityItem: EntityItem? = null

    override fun shouldExecute(): Boolean {
        return entityItem == null || (entityItem != null && entityItem!!.onGround)
    }

    override fun updateTask() {
        val item = this.entityIn.world.findNearestEntityWithinAABB(EntityItem::class.java,
                AxisAlignedBB(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)
                        .add(0.5, 0.0, 0.5))
                        .grow(7.0),
                this.entityIn) as EntityItem?

        item?.let {
            if (item.item.item.containerItem != null && item.item.item.containerItem != Items.AIR || item.item.item.containerItem != Blocks.AIR) {
                entityItem = it
                BugMagic.logger.debug("$entityIn found $entityItem")
            }
        }
    }
}
