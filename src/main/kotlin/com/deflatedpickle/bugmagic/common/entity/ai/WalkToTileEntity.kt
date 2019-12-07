/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.network.datasync.DataParameter
import net.minecraft.util.math.BlockPos

class WalkToTileEntity(private val entityIn: EntityLiving, private val check: () -> Boolean, private val inventory: DataParameter<BlockPos>) : EntityAIBase() {
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
