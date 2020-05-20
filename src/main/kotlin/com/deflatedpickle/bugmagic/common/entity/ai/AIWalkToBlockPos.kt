/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

/**
 * An AI task for an [EntityLiving] to walk to a [TileEntity]
 *
 * @author DeflatedPickle
 * @param check A check run to see if this task should execute
 * @param blockPos The [BlockPos] to walk to
 */
class AIWalkToBlockPos(
        private val entityIn: EntityLiving,
        private val check: () -> Boolean,
        private val blockPos: () -> BlockPos?
) : EntityAIBase() {
    override fun shouldExecute(): Boolean = with(blockPos()) {
        !entityIn.world.isAirBlock(this) &&
                entityIn.position.getDistance(this.x, this.y, this.z) > 1.0 &&
                this@AIWalkToBlockPos.check()
    }


    override fun updateTask() {
        val path = this.entityIn.navigator.getPathToPos(blockPos())

        path?.let {
            this.entityIn.navigator.setPath(path, entityIn.aiMoveSpeed.toDouble())
        }
    }
}
