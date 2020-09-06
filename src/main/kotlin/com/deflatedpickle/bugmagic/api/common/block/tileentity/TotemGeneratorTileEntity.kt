package com.deflatedpickle.bugmagic.api.common.block.tileentity

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.client.render.tileentity.TotemTileEntitySpecialRender
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.util.extension.getSlotItems
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable

/**
 * @see [TotemBlock]
 * @see [TotemTileEntitySpecialRender]
 */
class TotemGeneratorTileEntity(
	outputStackLimit: Int = 16,
	maxBugEssence: Int = 100,
	areaWidth: Int = 3,
	areaHeight: Int = 3,
	val trades: Map<Item, Array<Item>>,
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

			for (i in 0 until this.inputItemStackHandler.slots) {
				if (this.inputItemStackHandler.getStackInSlot(i) == ItemStack.EMPTY) {
					firstEmptySlot = i
					break
				}
			}

			println(this.inputItemStackHandler.getSlotItems())

			for (i in this.inputItemStackHandler.getSlotItems()) {
				if (this.trades.contains(i)) {
					println("-----------------------------------------")
				}
			}
		}
	}
}
