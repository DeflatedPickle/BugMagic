/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.AIWaitWithBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToBlockPos
import com.deflatedpickle.bugmagic.common.spell.AutoHoeSpell
import net.minecraft.entity.EntityLiving
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * The entity summoned with [AutoHoeSpell]
 *
 * @author DeflatedPickle
 * @param worldIn The world this entity belongs to
 */
class AutoHoeEntity(worldIn: World) : EntityCastable(worldIn) {
    init {
        setSize(0.8f, 0.5f)
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            1. Finds an inventory
            2. Finds a grass, dirt or path block
            3. Walks to the block
            4. Tills the soil
            5. Waits 20 ticks
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
                        this.world.getBlockState(it).block != Blocks.FARMLAND
                    } != null
                },
                origin = { this.dataManager.get(dataHomePosition) },
                radius = Vec3i(3, 1, 3),
                findFunc = { entityIn: EntityLiving, blockPos: BlockPos ->
                    val block = entityIn.world.getBlockState(blockPos).block
                    entityIn.world.isAirBlock(blockPos.offset(EnumFacing.UP)) && (block == Blocks.GRASS ||
                            block == Blocks.GRASS_PATH ||
                            block == Blocks.DIRT)
                }
        ) {}

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, AIWalkToBlockPos(this, { true }) { this.dataManager.get(dataHomePosition) })
        this.tasks.addTask(3, AIWaitWithBlock(findBlock = findBlock, entityIn = this,
                executeCheck = { entityLiving, blockPos ->
                    !entityLiving.world.isAirBlock(blockPos) &&
                            entityLiving.position.offset(EnumFacing.DOWN) == blockPos &&
                            entityLiving.world.getBlockState(blockPos).block != Blocks.FARMLAND
                },
                waitFor = 20) { blockPos: BlockPos, entityLiving: EntityLiving ->
            if (!entityLiving.world.isAirBlock(blockPos)) {
                entityLiving.world.setBlockState(blockPos, Blocks.FARMLAND.defaultState)
                findBlock.blockPos = null
                AIFindBlock.map[entityLiving.world]!!.remove(blockPos)
            }
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.32f
    }
}
