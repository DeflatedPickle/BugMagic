package com.deflatedpickle.bugmagic.entity.ai

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.passive.EntityTameable

class EntityAIHoverToOwner(private val entityIn: EntityTameable, private val floatHeightIn: Float, private val offsetX: Float, private val offsetZ: Float) : EntityAIBase() {
    lateinit var owner: EntityLivingBase

    override fun shouldExecute(): Boolean {
        val entityLivingBase = this.entityIn.owner

        return if (entityLivingBase != null) {
            owner = entityLivingBase
            true
        }
        else {
            false
        }
    }

    override fun updateTask() {
        // TODO: Make the mob face the same way as the owner
        // TODO: Fix the rotation movement, so that the mob sticks to the front-left of the owner

        // this.entityIn.rotationYaw = 0f
        // this.entityIn.setRotationYawHead(this.entityIn.owner!!.rotationYawHead)

        if (this.entityIn.posY < this.entityIn.owner!!.posY + 1.5f) {
            this.entityIn.moveRelative(this.entityIn.owner!!.posX.toFloat() - this.entityIn.posX.toFloat() - offsetX + Math.sin(this.entityIn.owner!!.rotationYaw.toDouble()).toFloat(),
                    this.floatHeightIn,
                    this.entityIn.owner!!.posZ.toFloat() - this.entityIn.posZ.toFloat() - offsetZ + Math.cos(this.entityIn.owner!!.rotationYaw.toDouble()).toFloat(),
                    0.75f)
        }
    }
}