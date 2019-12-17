package com.deflatedpickle.bugmagic.common.block.tileentity

import com.deflatedpickle.bugmagic.common.init.Fluid
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.items.ItemStackHandler

/**
 * Stores the items and fluid for the SpellTable
 *
 * @see com.deflatedpickle.bugmagic.common.block.SpellTable
 * @see com.deflatedpickle.bugmagic.client.render.tileentity.SpellTable
 */
class SpellTable(stackLimit: Int = 32) : TileEntity() {
    val itemStackHandler = ItemStackHandler(stackLimit)
    val fluidTank = FluidTank(Fluid.BUG_ESSENCE, 1, 16)

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        this.itemStackHandler.deserializeNBT(compound.getCompoundTag("inventory"))
        fluidTank.readFromNBT(compound.getCompoundTag("tank"))
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("inventory", this.itemStackHandler.serializeNBT())
        compound.setTag("tank", this.fluidTank.writeToNBT(NBTTagCompound()))
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