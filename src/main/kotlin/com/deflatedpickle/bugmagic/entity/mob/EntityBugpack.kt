package com.deflatedpickle.bugmagic.entity.mob

import com.deflatedpickle.bugmagic.entity.ai.EntityAIHoverToOwner
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class EntityBugpack(worldIn: World) : EntityTameable(worldIn) {
    init {
        this.isImmuneToFire = true

        setSize(0.65f, 0.7f)

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
        // this.tasks.addTask(2, EntityAIFollowOwner(this, 3.0, 0.5f, 50.0f))
        this.tasks.addTask(2, EntityAIHoverToOwner(this, 0.12f, -1.2f, 1.2f))
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 35.0
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.2
    }

    override fun createChild(ageable: EntityAgeable?): EntityAgeable? {
        return null
    }
}