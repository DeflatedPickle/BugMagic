package com.deflatedpickle.bugmagic.entity.mob

import com.deflatedpickle.bugmagic.entity.ai.EntityAIDigDown
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class EntityWurm(worldIn: World) : EntityTameable(worldIn) {
    init {
        this.isImmuneToFire = true

        setSize(0.7f, 0.45f)

        isTamed = true

        enablePersistence()
    }

    override fun canBeLeashedTo(player: EntityPlayer?): Boolean {
        return false
    }

    override fun canDespawn(): Boolean {
        return false
    }

    override fun initEntityAI() {
        // TODO: Add an AI to dig down to bedrock, if the block below is bedrock, die
        this.tasks.addTask(2, EntityAIDigDown(this))
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.2
    }

    override fun createChild(ageable: EntityAgeable?): EntityAgeable? {
        return null
    }

    override fun getEyeHeight(): Float {
        return 0.15f
    }
}