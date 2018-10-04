package com.deflatedpickle.bugmagic.tileentity

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

class TileEntityCauldron(val maxParts: Int, val maxWater: Float = 1f) : TileEntity() {
    private var partAmount = 0

    fun getParts(): Int {
        return partAmount
    }

    fun addParts(amount: Int) {
        if (partAmount + amount <= maxParts) {
            partAmount += amount
        }
    }

    var waterAmount = 0f

    var stirAmount = 0.0
    var hasStirrer = false

    var fullyStirred = false
    var stirsRequired = 0.0

    fun increaseWater(amount: Float) {
        if (waterAmount + amount <= maxWater) {
            waterAmount += amount
        }
        else {
            waterAmount = maxWater
        }
    }

    fun decreaseWater(amount: Float) {
        if (waterAmount - amount >= 0) {
            waterAmount -= amount
        }
    }

    fun resetWater() {
        waterAmount = 0f
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)

        compound.setInteger("partAmount", partAmount)
        compound.setFloat("waterAmount", waterAmount)
        compound.setDouble("stirAmount", stirAmount)
        compound.setBoolean("hasStirrer", hasStirrer)
        compound.setBoolean("fullyStirred", fullyStirred)
        compound.setDouble("stirRequired", stirsRequired)

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)

        partAmount = compound.getInteger("partAmount")
        waterAmount = compound.getFloat("waterAmount")
        stirAmount = compound.getDouble("stirAmount")
        hasStirrer = compound.getBoolean("hasStirrer")
        fullyStirred = compound.getBoolean("fullyStirred")
        stirsRequired = compound.getDouble("stirRequired")
    }
}