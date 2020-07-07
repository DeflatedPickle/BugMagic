package com.deflatedpickle.bugmagic.api.common.block.tileentity

import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.client.render.tileentity.TotemTileEntitySpecialRender
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.util.LimitedItemStackHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.ItemStackHandler

/**
 * A generic [TileEntity] for totems
 * @see [TotemBlock]
 * @see [TotemTileEntitySpecialRender]
 */
open class TotemTileEntity(
	totemType: TotemType,
	inputStackLimit: Int = 8,
	outputStackLimit: Int = 16,
	var currentBugEssence: Int = 0,
	maxBugEssence: Int = 100,
	var areaWidth: Int = 3,
	var areaHeight: Int = 3
) : TileEntity() {
	var maxBugEssence = maxBugEssence
		private set

	var totemType = totemType
		private set

	// Totems need stack handlers
	// So they can store inserted and converted items
	val inputItemStackHandler = ItemStackHandler(inputStackLimit)
	// Output slots are limited to 1 item each
	val outputItemStackHandler = LimitedItemStackHandler(outputStackLimit, 1)

	companion object {
		const val currentBugEssenceKey = "currentBugEssence"
		const val maxBugEssenceKey = "maxBugEssence"
		const val areaWidthKey = "areaWidth"
		const val areaHeightKey = "areaHeight"
		const val totemTypeKey = "totemType"
		const val inputInventory = "inputInventory"
		const val outputInventory = "outputInventory"
	}

	override fun readFromNBT(compound: NBTTagCompound) {
		super.readFromNBT(compound)
		this.currentBugEssence = compound.getInteger(currentBugEssenceKey)
		this.maxBugEssence = compound.getInteger(maxBugEssenceKey)
		this.areaWidth = compound.getInteger(areaWidthKey)
		this.areaHeight = compound.getInteger(areaHeightKey)
		this.totemType = TotemType.valueOf(compound.getString(totemTypeKey))
		this.inputItemStackHandler.deserializeNBT(compound.getCompoundTag(inputInventory))
		this.outputItemStackHandler.deserializeNBT(compound.getCompoundTag(outputInventory))
	}

	override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
		super.writeToNBT(compound)
		compound.setInteger(currentBugEssenceKey, this.currentBugEssence)
		compound.setInteger(maxBugEssenceKey, this.maxBugEssence)
		compound.setInteger(areaWidthKey, this.areaWidth)
		compound.setInteger(areaHeightKey, this.areaHeight)
		compound.setString(totemTypeKey, this.totemType.toString())
		compound.setTag(inputInventory, this.inputItemStackHandler.serializeNBT())
		compound.setTag(outputInventory, this.outputItemStackHandler.serializeNBT())
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
