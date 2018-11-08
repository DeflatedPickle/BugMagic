package com.deflatedpickle.bugmagic.particle

import net.minecraft.client.particle.ParticleBubble
import net.minecraft.world.World

class ParticleBubble(worldIn: World, xCoordIn: Double, yCoordIn: Double, zCoordIn: Double, xSpeedIn: Double, ySpeedIn: Double, zSpeedIn: Double) : ParticleBubble(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn) {
    init {
        particleMaxAge = 64
    }

    // Copied from Minecraft/ParticleBubble.java and removed the underwater restriction
    override fun onUpdate() {
        this.prevPosX = this.posX
        this.prevPosY = this.posY
        this.prevPosZ = this.posZ
        this.motionY += 0.002
        this.move(this.motionX, this.motionY, this.motionZ)
        this.motionX *= 0.8500000238418579
        this.motionY *= 0.8500000238418579
        this.motionZ *= 0.8500000238418579

        if (this.particleMaxAge-- <= 0) {
            this.setExpired()
        }
    }
}