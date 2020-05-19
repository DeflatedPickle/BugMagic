/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.network.datasync.DataParameter
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

/**
 * An AI task for an [EntityLiving] to walk to a [TileEntity]
 *
 * @author DeflatedPickle
 * @param check A check run to see if this task should execute
 * @param inventory The [BlockPos] to walk to
 */
class AIWalkToBlockPos(
        private val entityIn: EntityLiving,
        private val check: () -> Boolean,
        private val inventory: DataParameter<BlockPos>
) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        if (check() &&
                entityIn.dataManager.get(inventory) != BlockPos.ORIGIN) {
            return true
        }
        return false
    }

    override fun updateTask() {
        val path = this.entityIn.navigator.getPathToPos(entityIn.dataManager.get(inventory))

        if (path != null) {
            this.entityIn.navigator.setPath(path, entityIn.aiMoveSpeed.toDouble())
        }
    }
}
