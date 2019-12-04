package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class FindBlock(private val entityIn: EntityLiving, private val findFunc: (EntityLiving, BlockPos) -> Boolean) : EntityAIBase() {
    companion object {
        val map = WeakHashMap<World, MutableSet<BlockPos>>()
    }

    var blockPos: BlockPos? = null

    override fun shouldExecute(): Boolean = !(blockPos != null && !entityIn.world.isAirBlock(blockPos!!))

    override fun updateTask() {
        blockPos = BlockPos.getAllInBox(
                entityIn.posX.toInt() - 15, entityIn.posY.toInt(), entityIn.posZ.toInt() - 15,
                entityIn.posX.toInt() + 15, entityIn.posY.toInt() + 15, entityIn.posZ.toInt() + 15
        ).find {
            this.findFunc(this.entityIn, it)
                    && !map.getOrDefault(entityIn.world, mutableSetOf()).contains(it)
        }

        blockPos?.let {
            map.computeIfAbsent(entityIn.world) {
                mutableSetOf()
            }.add(blockPos!!)
        }
    }
}