/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.AIWaitWithBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToBlock
import com.deflatedpickle.bugmagic.common.spell.AutoFertilizerSpell
import java.util.concurrent.ThreadLocalRandom
import net.minecraft.block.IGrowable
import net.minecraft.entity.EntityLiving
import net.minecraft.inventory.IInventory
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * The entity summoned by [AutoFertilizerSpell]
 *
 * @author DeflatedPickle
 * @param worldIn The world this entity belongs to
 */
class AutoFertilizerEntity(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataHomePosition: DataParameter<BlockPos> = EntityDataManager.createKey(
                AutoFertilizerEntity::class.java,
                DataSerializers.BLOCK_POS
        )
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataHomePosition, this.position)
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            - Finds an inventory
            - Finds a plant-able block
            - Walks to the block
            - Grow the plant
            - Wait 40-60 seconds
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
                            (block as IGrowable).canGrow(entityIn.world, blockPos, state, false)
                }
        ) {}

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, AIWalkToBlock(findBlock, this) { true })
        this.tasks.addTask(3, AIWaitWithBlock(findBlock = findBlock, entityIn = this,
                executeCheck = { entityLiving, blockPos ->
                    !entityLiving.world.isAirBlock(blockPos) &&
                            entityLiving.world.getBlockState(blockPos).block is IGrowable
                },
                waitFor = ThreadLocalRandom.current().nextInt(240, 360)) { blockPos: BlockPos, entityLiving: EntityLiving ->
            if (!entityLiving.world.isAirBlock(blockPos)) {
                val state = entityLiving.world.getBlockState(blockPos)
                val block = state.block
                (block as IGrowable).grow(entityLiving.world, AutoHarvesterEntity.random, blockPos, state)
                findBlock.blockPos = null
                AIFindBlock.map[entityLiving.world]!!.remove(blockPos)
            }
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.4f
    }
}
