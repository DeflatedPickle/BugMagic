/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class RemoveBlock(private val findBlock: FindBlock, private val entityIn: EntityLiving, private var breakWith: ItemStack = ItemStack(Items.STONE_SHOVEL), private val postBreak: (EntityLiving) -> Unit) : EntityAIBase() {
    var waitCurrent = 0f

    override fun shouldExecute(): Boolean {
        val blockPos = (entityIn.tasks.taskEntries.filter { it.action is FindBlock }[0].action as FindBlock).blockPos
        return blockPos != null && entityIn.position == blockPos
    }

    override fun updateTask() {
        findBlock.blockPos?.let {
            val breakSpeed = if (breakWith.getDestroySpeed(entityIn.world.getBlockState(it)) > 32f) {
                breakWith.getDestroySpeed(entityIn.world.getBlockState(it))
            } else {
                32f
            }

            if (waitCurrent == breakSpeed) {
                waitCurrent = 0f

                entityIn.world.setBlockToAir(it)
                findBlock.blockPos = null

                postBreak(entityIn)
            } else {
                waitCurrent++
            }
        }
    }
}
