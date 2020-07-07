package com.deflatedpickle.bugmagic.api.common.block.tileentity

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.client.render.tileentity.TotemTileEntitySpecialRender
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.util.LimitedItemStackHandler
import com.deflatedpickle.bugmagic.api.common.util.extension.update
import com.deflatedpickle.bugmagic.common.init.ItemInit
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraftforge.items.ItemStackHandler

/**
 * @see [TotemBlock]
 * @see [TotemTileEntitySpecialRender]
 */
class TotemGathererTileEntity(
	outputStackLimit: Int = 16,
	maxBugEssence: Int = 100,
	areaWidth: Int = 3,
	areaHeight: Int = 3,
	val item: Item,
	@Suppress("MemberVisibilityCanBePrivate")
	val upperBound: Int
) : TotemTileEntity(
	TotemType.GENERATOR,
	0, outputStackLimit,
	0, maxBugEssence,
	areaWidth, areaHeight
), ITickable {
	override fun update() {
		val chance = BugMagic.random.nextInt(this.upperBound)

		if (chance == 0) {
			var firstEmptySlot = 0

			for (i in 0 until this.outputItemStackHandler.slots) {
				if (this.outputItemStackHandler.getStackInSlot(i) == ItemStack.EMPTY) {
					firstEmptySlot = i
					break
				}
			}

			this.outputItemStackHandler.insertItem(
				firstEmptySlot,
				ItemStack(this.item),
				false
			)
		}
	}
}
