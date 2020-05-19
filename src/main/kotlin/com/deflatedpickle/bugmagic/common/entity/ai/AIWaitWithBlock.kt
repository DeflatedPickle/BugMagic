/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos

/**
 * An AI task to wait with a block
 *
 * @author DeflatedPickle
 * @param findBlock The [AIFindBlock] attached to [entityIn]
 * @param entityIn The [EntityLiving] this task is applied to
 * @param executeCheck A check performed before this task is executed
 * @param waitFor A length of time to wait for
 * @param postWait A lambda called when the [entityIn] has finished waiting
 */
// TODO: AIWaitWithBlock shouldn't be dependant on AIFindBlock
class AIWaitWithBlock(
    private val findBlock: AIFindBlock,
    private val entityIn: EntityLiving,
    private val executeCheck: (EntityLiving, BlockPos) -> Boolean,
    private val waitFor: Int,
    internal val postWait: (BlockPos, EntityLiving) -> Unit
) : EntityAIBase() {
    var waitCurrent = 0

    override fun shouldExecute(): Boolean {
        val blockPos = findBlock.blockPos
        return blockPos != null &&
                entityIn.position.getDistance(blockPos.x, blockPos.y, blockPos.z) <= 1.2 &&
                executeCheck(entityIn, blockPos)
    }

    override fun updateTask() {
        findBlock.blockPos?.let {
            if (waitCurrent == waitFor) {
                waitCurrent = 0

                BugMagic.logger.debug("$entityIn finished waiting with ${findBlock.blockPos}")
                postWait(it, entityIn)
            } else {
                waitCurrent++
            }
        }
    }
}
