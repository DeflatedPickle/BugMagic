package com.deflatedpickle.bugmagic.api.client.render.tileentity

import com.deflatedpickle.bugmagic.api.client.util.extension.drawNameTag
import com.deflatedpickle.bugmagic.api.client.util.extension.render
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import com.deflatedpickle.bugmagic.api.common.util.extension.isNotEmpty
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTableTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * @see [TotemBlock]
 * @see [TotemTileEntity]
 */
class TotemTileEntitySpecialRender : TileEntitySpecialRenderer<TotemTileEntity>() {
	override fun render(
		te: TotemTileEntity, x: Double, y: Double, z: Double,
		partialTicks: Float, destroyStage: Int, alpha: Float
	) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha)

		this.renderItemRing(
			te, te.inputItemStackHandler, 0.6,
			x, y, z,
			1f, 1f,
			0.5f, 0.8f
		)
		this.renderItemRing(
			te, te.outputItemStackHandler, 0.9,
			x, y, z,
			-1f, -1f,
			-0.5f, -0.8f
		)
	}

	private fun renderItemRing(
		te: TotemTileEntity, stackHandler: ItemStackHandler, radius: Double,
		x: Double, y: Double, z: Double,
		xRotationMultiplier: Float, yRotationMultiplier: Float,
		xMultiplier: Float, yMultiplier: Float
	) {
		GlStateManager.pushMatrix()
		GlStateManager.translate(x + 0.5, y + 1, z + 0.5)

		val itemCount = 0.until(stackHandler.slots).count {
			stackHandler.getStackInSlot(it) != ItemStack.EMPTY
		}

		GlStateManager.rotate(
			te.world.totalWorldTime.toFloat(),
			0.03f * xRotationMultiplier, 0.2f, 0.03f * yRotationMultiplier
		)
		GlStateManager.pushMatrix()

		for (i in 0 until stackHandler.slots) {
			val stack = stackHandler.getStackInSlot(i)

			if (stack.isNotEmpty()) {
				this.renderItemRing(te, x, y, stack, i, itemCount, radius, xMultiplier, yMultiplier)
			}
		}

		GlStateManager.popMatrix()
		GlStateManager.popMatrix()
	}

	private fun renderItemRing(
		te: TotemTileEntity, x: Double, y: Double,
		stack: ItemStack, i: Int, itemCount: Int, radius: Double,
		xMultiplier: Float, yMultiplier: Float
	) {
		GlStateManager.pushMatrix()

		val bigTheta = (PI * 2) * i / itemCount

		val bigX = radius * cos(bigTheta)
		val bigZ = radius * sin(bigTheta)

		val bigSpeed = 0.1f
		val bigAmplitude = 0.06
		GlStateManager.translate(
			bigX,
			sin(te.world.totalWorldTime.toFloat() * bigSpeed) *
				bigAmplitude,
			bigZ)

		GlStateManager.pushMatrix()
		GlStateManager.rotate(
			te.world.totalWorldTime.toFloat(),
			0f, -0.4f, 0f
		)
		Minecraft.getMinecraft().fontRenderer.drawNameTag(
			"${stack.displayName} x${stack.count}",
			x.toInt(), y.toInt()
		)
		GlStateManager.popMatrix()

		GlStateManager.rotate(
			te.world.totalWorldTime.toFloat(),
			i * xMultiplier, 1.2f, i * yMultiplier
		)

		GlStateManager.scale(0.5f, 0.5f, 0.5f)
		stack.render(world)

		GlStateManager.popMatrix()
	}
}
