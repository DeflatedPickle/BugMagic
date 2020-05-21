/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun ItemStack.isNotEmpty() = !this.isEmpty

fun ItemStack.drop(world: World, x: Double, y: Double, z: Double): EntityItem =
        EntityItem(world, x, y, z, this).apply { world.spawnEntity(this) }

fun ItemStack.drop(world: World, blockPos: BlockPos): EntityItem =
        this.drop(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
