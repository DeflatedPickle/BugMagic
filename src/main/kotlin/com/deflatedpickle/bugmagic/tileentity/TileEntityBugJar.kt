package com.deflatedpickle.bugmagic.tileentity

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

class TileEntityBugJar(val maxBugs: Int) : TileEntity() {
    private var currentBugs = 0

    fun getBugs(): Int {
        return currentBugs
    }

    fun addBug(amount: Int) {
        if (currentBugs + amount <= maxBugs) {
            currentBugs += amount
        }
    }

    fun removeBug() {
        // TODO: Spawn a bug in the world to fly up and then die
        currentBugs--
    }

    fun clearBugs() {
        currentBugs = 0
    }

    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        super.writeToNBT(compound)

        compound!!.setInteger("bugs", currentBugs)

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        super.readFromNBT(compound)

        currentBugs = compound!!.getInteger("bugs")
    }
}