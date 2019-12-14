package com.deflatedpickle.bugmagic.common.block.tileentity

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.ItemStackHandler

class SpellTable(stackLimit: Int = 32) : TileEntity() {
    val itemStackHandler = ItemStackHandler(stackLimit)

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        this.itemStackHandler.deserializeNBT(compound.getCompoundTag("inventory"))
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("inventory", this.itemStackHandler.serializeNBT())
        return compound
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(this.pos, 3, this.updateTag)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return this.writeToNBT(NBTTagCompound())
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        super.onDataPacket(net, pkt)
        handleUpdateTag(pkt.nbtCompound)
    }
}