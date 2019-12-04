/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.FindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.RemoveBlock
import com.deflatedpickle.bugmagic.common.entity.ai.WalkToBlock
import java.util.concurrent.ThreadLocalRandom
import net.minecraft.block.BlockFlower
import net.minecraft.entity.EntityLiving
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class EssenceCollector(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataEssence: DataParameter<Float> = EntityDataManager.createKey(EssenceCollector::class.java, DataSerializers.FLOAT)
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataEssence, 0f)
    }

    override fun initEntityAI() {
        val findBlock = FindBlock(this) { entityIn: EntityLiving, blockPos: BlockPos ->
            entityIn.world.getBlockState(blockPos).block is BlockFlower && entityIn.navigator.getPathToPos(blockPos) != null
        }

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, WalkToBlock(findBlock, this))
        this.tasks.addTask(3, RemoveBlock(findBlock, this) { entityIn ->
            entityIn.dataManager.set(dataEssence, entityIn.dataManager.get(dataEssence) + ThreadLocalRandom.current().nextInt(18, 34))
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.32f
    }
}
