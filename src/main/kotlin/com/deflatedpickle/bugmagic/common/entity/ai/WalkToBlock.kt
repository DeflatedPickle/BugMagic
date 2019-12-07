/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.pathfinding.Path

class WalkToBlock(private val findBlock: FindBlock, private val entityIn: EntityLiving, private val check: () -> Boolean) : EntityAIBase() {
    var path: Path? = null

    override fun shouldExecute(): Boolean {
        val blockPos = findBlock.blockPos

        return blockPos != null &&
                !entityIn.world.isAirBlock(blockPos) &&
                entityIn.position.getDistance(blockPos.x, blockPos.y, blockPos.z) > 1.0 &&
                this.check()
    }

    override fun updateTask() {
        val blockPos = findBlock.blockPos

        blockPos?.let {
            path = this.entityIn.navigator.getPathToPos(blockPos)

            path?.let {
                this.entityIn.navigator.setPath(path, entityIn.aiMoveSpeed.toDouble())
            }
        }
    }
}
