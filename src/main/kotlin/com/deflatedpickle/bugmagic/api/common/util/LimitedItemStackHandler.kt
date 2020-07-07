package com.deflatedpickle.bugmagic.api.common.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

class LimitedItemStackHandler(
	size: Int,
	private val stackSize: Int
) : ItemStackHandler(size) {
	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		return when {
			this.getStackInSlot(slot).count < this.stackSize -> {
				super.insertItem(slot, stack, simulate)
			}
			stack.count < this.stackSize -> {
				super.insertItem(slot, stack.splitStack(1), simulate)
			}
			else -> {
				ItemStack.EMPTY
			}
		}
	}
}
