package com.deflatedpickle.bugmagic.common.block.tileentity

import net.minecraftforge.fluids.Fluid as VFluid
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * Stores the items and fluid for the SpellTable
 *
 * @see com.deflatedpickle.bugmagic.common.block.SpellTable
 * @see com.deflatedpickle.bugmagic.client.render.tileentity.SpellTable
 */
class SpellTable(stackLimit: Int = 32) : TileEntity() {
    val itemStackHandler = ItemStackHandler(stackLimit)
    val fluidTank = FluidTank(VFluid.BUCKET_VOLUME)
    val wandStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int {
            return 1
        }
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        this.itemStackHandler.deserializeNBT(compound.getCompoundTag("inventory"))
        this.fluidTank.readFromNBT(compound)
        this.wandStackHandler.deserializeNBT(compound.getCompoundTag("wand"))
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("inventory", this.itemStackHandler.serializeNBT())
        this.fluidTank.writeToNBT(compound)
        compound.setTag("wand", this.wandStackHandler.serializeNBT())
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

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> when (facing) {
                EnumFacing.EAST -> wandStackHandler as T
                else -> itemStackHandler as T
            }
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY -> fluidTank as T
            else -> super.getCapability(capability, facing)
        }
    }
}