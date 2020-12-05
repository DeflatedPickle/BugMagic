/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.AIWaitWithBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToBlockPos
import com.deflatedpickle.bugmagic.common.spell.AutoHarvesterSpell
import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import net.minecraft.block.IGrowable
import net.minecraft.entity.EntityLiving
import net.minecraft.inventory.IInventory
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * The entity summoned with [AutoHarvesterSpell]
 *
 * @author DeflatedPickle
 * @param worldIn The world this entity belongs to
 */
class AutoHarvesterEntity(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val random = Random()
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            1. Finds an inventory
            2. Finds a plant-able block
            3. Walks to the block
            4. Harvest the plant
            5. Wait 10-20 seconds
            - Usually repeats steps 2 through 5
         */

        this.tasks.addTask(1, AIFindClosestTileEntity(this, dataHomePosition, {
            it is IInventory
        }, {
            if (it != null) {
                this.dataManager.set(dataHomePosition, it.pos)
            }
        }))

        val findBlock = AIFindBlock(
                entityIn = this,
                check = { blockPos: BlockPos, vec3i: Vec3i, _: BlockPos? ->
                    BlockPos.getAllInBox(
                            blockPos.x - vec3i.x, blockPos.y - vec3i.y, blockPos.z - vec3i.z,
                            blockPos.x + vec3i.x, blockPos.y + vec3i.y, blockPos.z + vec3i.z
                    ).find {
                        this.world.getBlockState(it).block is IGrowable
                    } != null
                },
                origin = { this.dataManager.get(dataHomePosition) },
                radius = Vec3i(3, 1, 3),
                findFunc = { entityIn: EntityLiving, blockPos: BlockPos ->
                    val state = entityIn.world.getBlockState(blockPos)
                    val block = state.block
                    entityIn.world.isAirBlock(blockPos.offset(EnumFacing.UP)) &&
                            block is IGrowable &&
                            !(block as IGrowable).canGrow(entityIn.world, blockPos, state, false)
                }
        ) {}

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, AIWalkToBlockPos(this, { true }) { this.dataManager.get(dataHomePosition) })
        this.tasks.addTask(3, AIWaitWithBlock(findBlock = findBlock, entityIn = this,
                executeCheck = { entityLiving, blockPos ->
                    !entityLiving.world.isAirBlock(blockPos) &&
                            entityLiving.world.getBlockState(blockPos).block is IGrowable
                },
                waitFor = ThreadLocalRandom.current().nextInt(50, 130)) { blockPos: BlockPos, entityLiving: EntityLiving ->
            if (!entityLiving.world.isAirBlock(blockPos)) {
                val state = entityLiving.world.getBlockState(blockPos)
                val block = state.block
                entityLiving.world.destroyBlock(blockPos, true)
                findBlock.blockPos = null
                AIFindBlock.map[entityLiving.world]!!.remove(blockPos)
            }
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.26f
    }
}
