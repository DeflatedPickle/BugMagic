/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos

/**
 * An AI task to find an item
 *
 * @author DeflatedPickle
 * @param entityIn The [EntityLiving] this task is applied to
 * @param origin The [BlockPos] to look around
 * @param distance The length of
 */
class AIFindItem(
    private val entityIn: EntityLiving,
    private val origin: () -> BlockPos,
    private val distance: Double
) : EntityAIBase() {
    var entityItem: EntityItem? = null

    override fun shouldExecute(): Boolean {
        return entityItem == null || (entityItem != null && entityItem!!.onGround)
    }

    override fun updateTask() {
        val item = this.entityIn.world.findNearestEntityWithinAABB(EntityItem::class.java,
                AxisAlignedBB(origin()
                        .add(0.5, 0.0, 0.5))
                        .grow(distance),
                this.entityIn) as EntityItem?

        item?.let {
            if (item.item.item.containerItem != null &&
                    item.item.item.containerItem != Items.AIR ||
                    item.item.item.containerItem != Blocks.AIR) {
                entityItem = it
                BugMagic.logger.debug("$entityIn found $entityItem")
            }
        }
    }
}
