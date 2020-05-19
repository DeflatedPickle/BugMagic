/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.BugMagic
import java.util.WeakHashMap
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * An AI task to find a block, using multiple checks
 *
 * @author DeflatedPickle
 * @param entityIn The [EntityLiving] this task is applied to
 * @param check A check to see if the task should run
 * @param origin The origin of the [entityIn]
 * @param radius The search radius around the [origin]
 * @param findFunc A lambda ran when checking a box the size of [radius] around [origin] to see if the current block should be used
 * @param afterFound A lambda ran on the first valid block, found with [findFunc]
 */
class AIFindBlock(
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
