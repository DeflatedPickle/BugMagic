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
    set(value) {
        if (waterAmount + value <= maxWater) {
            field += value
        }
    }

    var stirAmount = 0
    var hasStirrer = false

    var fullyStirred = false

    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        super.writeToNBT(compound)

        compound!!.setInteger("partAmount", partAmount)
        compound.setFloat("waterAmount", waterAmount)
        compound.setInteger("stirAmount", stirAmount)
        compound.setBoolean("hasStirrer", hasStirrer)
        compound.setBoolean("fullyStirred", fullyStirred)

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        super.readFromNBT(compound)

        partAmount = compound!!.getInteger("partAmount")
        waterAmount = compound.getFloat("waterAmount")
        stirAmount = compound.getInteger("stirAmount")
        hasStirrer = compound.getBoolean("hasStirrer")
        fullyStirred = compound.getBoolean("fullyStirred")
    }
}