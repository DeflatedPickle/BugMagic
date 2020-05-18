/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import java.util.WeakHashMap
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class FindBlock(
        private val entityIn: EntityLiving,
        private val check: (BlockPos, Vec3i, BlockPos?) -> Boolean,
        private val origin: () -> BlockPos,
        private val radius: Vec3i,
        private val findFunc: (EntityLiving, BlockPos) -> Boolean,
        private val afterFound: (BlockPos) -> Unit
) : EntityAIBase() {
    companion object {
        val map = WeakHashMap<World, MutableSet<BlockPos>>()
    }

    var blockPos: BlockPos? = null

    override fun shouldExecute(): Boolean {
        return blockPos == null && this.check(this.origin(), this.radius, this.blockPos)
    }

    override fun updateTask() {
        val originGet = this.origin()
        blockPos = BlockPos.getAllInBox(
                originGet.x - radius.x, originGet.y - radius.y, originGet.z - radius.z,
                originGet.x + radius.x, originGet.y + radius.y, originGet.z + radius.z
        ).find {
            this.findFunc(this.entityIn, it) &&
                    !map.getOrDefault(entityIn.world, mutableSetOf()).contains(it)
        }

        blockPos?.let {
            BugMagic.logger.debug("$entityIn found a block at $blockPos")
            afterFound(it)

            map.computeIfAbsent(entityIn.world) {
                mutableSetOf()
            }.add(blockPos!!)
        }
    }
}
