package com.deflatedpickle.bugmagic.api.common.util

import net.minecraft.util.math.AxisAlignedBB

object AABBUtil {
	private val EMPTY_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
	private val bbUnit = 1.0 / 16.0

	fun empty() = EMPTY_AABB

	fun unitDouble(): Double = bbUnit
	fun unitFloat(): Float = bbUnit.toFloat()
}
