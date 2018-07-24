package com.deflatedpickle.bugmagic.entity.ai

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.passive.EntityTameable

class EntityAIModHoverToOwner(val entityIn: EntityTameable, val floatHeightIn: Float, val offsetX: Float, val offsetZ: Float) : EntityAIBase() {
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
        super.updateTask()

        if (this.entityIn.posY < this.entityIn.owner!!.posY + 1.5f) {
            // this.entityIn.moveRelative(0f, this.floatHeightIn, 0f, 1f)
            // TODO: Make the entity rotate with the owner
            this.entityIn.moveRelative(this.entityIn.owner!!.posX.toFloat() - this.entityIn.posX.toFloat() + offsetX,
                    this.floatHeightIn,
                    this.entityIn.owner!!.posZ.toFloat() - this.entityIn.posZ.toFloat() + offsetZ,
                    0.75f)
        }
    }
}