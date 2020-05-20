/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIWaitWithBlock
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToBlockPos
import com.deflatedpickle.bugmagic.common.spell.EssenceCollectorSpell
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min
import net.minecraft.block.BlockFlower
import net.minecraft.entity.EntityLiving
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * The entity summoned with [EssenceCollectorSpell]
 *
 * @author DeflatedPickle
 * @param worldIn The world this entity belongs to
 */
class EssenceCollectorEntity(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataEssence: DataParameter<Float> = EntityDataManager.createKey(EssenceCollectorEntity::class.java, DataSerializers.FLOAT)
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataEssence, 0f)
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            - Finds a flower
            - Walks to the flower
            - Eats the flower
         */

        val findBlock = AIFindBlock(entityIn = this,
                check = { blockPos: BlockPos, vec3i: Vec3i, _: BlockPos? ->
                    !this.world.isAirBlock(this.position)
                },
                origin = { this.position },
                radius = Vec3i(15, 7, 15),
                findFunc = { entityIn: EntityLiving, blockPos: BlockPos ->
                    entityIn.world.getBlockState(blockPos).block is BlockFlower
                }) {}

        this.tasks.addTask(1, findBlock)
        this.tasks.addTask(2, AIWalkToBlockPos(this, { dataManager.get(dataEssence) < 100 }, { findBlock.blockPos }))
        this.tasks.addTask(3, AIWaitWithBlock(findBlock, this,
                { entityLiving: EntityLiving, blockPos: BlockPos -> true },
                32) { blockPosIn, entityIn ->
            entityIn.world.setBlockToAir(blockPosIn)
            findBlock.blockPos = null

            entityIn.dataManager.set(dataEssence,
                    min(entityIn.dataManager.get(dataEssence) +
                            ThreadLocalRandom.current().nextInt(18, 34), 100f))
        })
    }

    override fun getAIMoveSpeed(): Float {
        return 0.18f
    }
}
