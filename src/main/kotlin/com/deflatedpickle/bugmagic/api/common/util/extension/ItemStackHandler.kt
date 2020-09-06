/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.ItemStackHandler

fun ItemStackHandler.dropSlot(slot: Int, worldIn: World, pos: BlockPos) {
    if (this.getStackInSlot(slot).isNotEmpty()) {
        val dropStack = this.extractItem(0, 1, false)
        val entity = EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble() + 1, pos.z.toDouble(), dropStack)
        worldIn.spawnEntity(entity)
    }
}

fun ItemStackHandler.getSlotStacks(): Array<ItemStack?> {
    val stacks = arrayOfNulls<ItemStack>(this.slots)

    for (i in 0 until this.slots) {
		val stack = this.getStackInSlot(i)

		if (!stack.isEmpty) {
			stacks[i] = stack
		}
    }

    return stacks
}

fun ItemStackHandler.getSlotItems(): Array<Item?> =
	this.getSlotStacks().map { it?.item }.toTypedArray()
