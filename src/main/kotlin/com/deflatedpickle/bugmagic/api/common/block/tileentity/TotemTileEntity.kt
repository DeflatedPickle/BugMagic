package com.deflatedpickle.bugmagic.api.common.block.tileentity

import com.deflatedpickle.bugmagic.api.TotemType
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

class TotemTileEntity(
	val totemType: TotemType,
	var currentBugEssence: Int = 0,
	val maxBugEssence: Int = 100,
	var areaWidth: Int = 3,
	var areaHeight: Int = 3
) : TileEntity() {
	override fun readFromNBT(compound: NBTTagCompound) {
		super.readFromNBT(compound)
		this.currentBugEssence = compound.getInteger("currentBugEssence")
		this.areaWidth = compound.getInteger("areaWidth")
		this.areaHeight = compound.getInteger("areaHeight")
	}

	override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
		super.writeToNBT(compound)
		compound.setString("totemType", this.totemType.toString())
		compound.setInteger("currentBugEssence", this.currentBugEssence)
		compound.setInteger("maxBugEssence", this.maxBugEssence)
		compound.setInteger("areaWidth", this.areaWidth)
		compound.setInteger("areaHeight", this.areaHeight)
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
