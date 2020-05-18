/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos

class WaitWithBlock(private val findBlock: FindBlock, private val entityIn: EntityLiving, private val executeCheck: (EntityLiving, BlockPos) -> Boolean, private val waitFor: Int, internal val postWait: (BlockPos, EntityLiving) -> Unit) : EntityAIBase() {
    var waitCurrent = 0

    override fun shouldExecute(): Boolean {
        val blockPos = findBlock.blockPos
        return blockPos != null && entityIn.position.getDistance(blockPos.x, blockPos.y, blockPos.z) <= 1.2 && executeCheck(entityIn, blockPos)
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
