/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.FindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.FindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.TakeFromInventory
import com.deflatedpickle.bugmagic.common.entity.ai.WaitWithBlock
import com.deflatedpickle.bugmagic.common.entity.ai.WalkToBlock
import com.deflatedpickle.bugmagic.common.entity.ai.WalkToTileEntity
import net.minecraft.entity.EntityLiving
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemSeeds
import net.minecraft.item.ItemStack
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class AutoPlanter(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataHomePosition: DataParameter<BlockPos> = EntityDataManager.createKey(AutoPlanter::class.java, DataSerializers.BLOCK_POS)
        val dataSeeds: DataParameter<ItemStack> = EntityDataManager.createKey(AutoPlanter::class.java, DataSerializers.ITEM_STACK)
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataSeeds, ItemStack.EMPTY)
        this.dataManager.register(dataHomePosition, BlockPos.ORIGIN)
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            - Find a farm block
            - Walk to inventory
            - Take a seed
            - Walk to farm block
            - Plant seed
         */

        this.tasks.addTask(1, FindClosestTileEntity(this, dataHomePosition, {
            it is IInventory
        }, {
            if (it != null) {
                this.dataManager.set(dataHomePosition, it.pos)
            }
        }))

        val findBlock = FindBlock(entityIn = this,
                check = { blockPos: BlockPos, vec3i: Vec3i, _: BlockPos? ->
                    BlockPos.getAllInBox(
                            blockPos.x - vec3i.x, blockPos.y - vec3i.y, blockPos.z - vec3i.z,
                            blockPos.x + vec3i.x, blockPos.y + vec3i.y, blockPos.z + vec3i.z
                    ).find {
                        this.world.getBlockState(it).block == Blocks.FARMLAND
                    } != null
                },
                origin = { this.dataManager.get(dataHomePosition) },
                radius = Vec3i(3, 3, 3),
                findFunc = { entityIn: EntityLiving, blockPos: BlockPos ->
                    entityIn.world.getBlockState(blockPos).run {
                        val item = dataManager.get(dataSeeds).item
                        if (item is ItemSeeds &&
                                entityIn.world.getBlockState(blockPos.offset(EnumFacing.UP)).block == Blocks.AIR)
                            this.block.canSustainPlant(this,
                                entityIn.world, blockPos, EnumFacing.UP, item)
                        else false
                    }
                }
        ) {}

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, WalkToTileEntity(this, { this.dataManager.get(dataSeeds) == ItemStack.EMPTY }, dataHomePosition))
        this.tasks.addTask(2, TakeFromInventory(this))
        this.tasks.addTask(3, WalkToBlock(findBlock, this) {
            dataManager.get(dataSeeds) != ItemStack.EMPTY
        })
        this.tasks.addTask(4, WaitWithBlock(findBlock = findBlock, entityIn = this,
                executeCheck = { entityLiving, blockPos ->
                    entityLiving.world.getBlockState(blockPos.offset(EnumFacing.UP)).block == Blocks.AIR
                },
                waitFor = 14) { blockPos: BlockPos, entityLiving: EntityLiving ->
            if (!entityLiving.world.isAirBlock(blockPos) && dataManager.get(dataSeeds) != ItemStack.EMPTY) {
                entityLiving.world.setBlockState(blockPos.up(),
                        (dataManager.get(dataSeeds).item as ItemSeeds).getPlant(entityLiving.world, blockPos))
                findBlock.blockPos = null
                dataManager.set(dataSeeds, ItemStack.EMPTY)
                FindBlock.map[entityLiving.world]!!.remove(blockPos)
            }
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.32f
    }
}
