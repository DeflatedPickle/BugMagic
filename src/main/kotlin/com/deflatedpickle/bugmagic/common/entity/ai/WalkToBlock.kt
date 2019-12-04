/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase

class WalkToBlock(private val findBlock: FindBlock, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        val blockPos = (entityIn.tasks.taskEntries.filter { it.action is FindBlock }[0].action as FindBlock).blockPos
        return blockPos != null && !entityIn.world.isAirBlock(blockPos)
    }

    override fun updateTask() {
        val blockPos = findBlock.blockPos

        blockPos?.let {
            val path = this.entityIn.navigator.getPathToPos(blockPos)

            path?.let {
                this.entityIn.navigator.setPath(path, entityIn.aiMoveSpeed.toDouble())
            }
        }
    }
}
