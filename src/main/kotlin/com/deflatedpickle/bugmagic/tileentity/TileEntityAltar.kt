package com.deflatedpickle.bugmagic.tileentity

import net.minecraft.item.Item
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

class TileEntityAltar(val maxParts: Int) : TileEntity() {
    private val currentParts = mutableListOf<Item>()

    fun getParts(): List<Item> {
        return currentParts
    }

    fun addPart(part: Item) {
        if (currentParts.size + 1 <= maxParts) {
            currentParts.add(part)
        }
    }

    fun clearParts() {
        currentParts.clear()
    }

    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        super.writeToNBT(compound!!)

        // TODO: Write parts to NBT

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        super.readFromNBT(compound!!)

        // TODO: Read parts from NBT
    }
}