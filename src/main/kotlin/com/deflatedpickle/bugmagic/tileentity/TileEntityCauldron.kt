package com.deflatedpickle.bugmagic.tileentity

import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.items.ItemBugPart
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Tuple

class TileEntityCauldron(val maxParts: Int, val maxWater: Float = 1f) : TileEntity() {
    private var partAmount = 0
    private var partList = IntArray(6)

    // Render variables
    var angle = 0f

    var stirMaxAmount = 0.0

    var wasStirred = false
    var stirRotationAmount = 0.0  // Speed
    // TODO: Apply more drag as it becomes more bug essence
    val stirRotationDrag = 0.55
    // private val stirEmptyRotationDrag = 2.0
    val stirRotationMomentum = 0.1

    var stirCurrentDrag = stirRotationDrag

    var stirRotationMomentumTick = 0

    val stirRotationMin = 10
    val stirRotationMax = 13

    val partRotationList = mutableListOf<Float>()
    val partPositionList = mutableListOf<Tuple<Double, Double>>()
    val partSizeList = mutableListOf<Float>()

    fun getPartAmount(): Int {
        return partAmount
    }

    fun addPartAmount(amount: Int) {
        if (partAmount + amount <= maxParts) {
            partAmount += amount
        }
    }

    fun getParts(): IntArray {
        return partList
    }

    fun addPart(part: ItemBugPart) {
        partList[partAmount] = ModItems.bugParts.indexOf(part)
        addPartAmount(1)
    }

    fun resetParts() {
        partList = IntArray(6)
        partAmount = 0
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
        compound.setIntArray("partList", partList)
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
        partList = compound.getIntArray("partList")
        waterAmount = compound.getFloat("waterAmount")
        stirAmount = compound.getDouble("stirAmount")
        hasStirrer = compound.getBoolean("hasStirrer")
        fullyStirred = compound.getBoolean("fullyStirred")
        stirsRequired = compound.getDouble("stirRequired")
    }
}