/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.pathfinding.Path

/**
 * An AI task for an [EntityLiving] to walk to a [BlockPos]
 *
 * @author DeflatedPickle
 * @param findBlock The [AIFindBlock] task attached to [entityIn]
 * @param entityIn The [EntityLiving] this task is applied to
 * @param check A check run to see if this task should execute
 */
// TODO: AIWalkToBlock shouldn't be dependant on AIFindBlock
// TODO: Merge AIWalkToBlock with AIWalkToBlockPos
class AIWalkToBlock(
    private val findBlock: AIFindBlock,
    private val entityIn: EntityLiving,
    private val check: () -> Boolean
) : EntityAIBase() {
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
