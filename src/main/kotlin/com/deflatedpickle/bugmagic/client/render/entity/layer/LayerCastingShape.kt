package com.deflatedpickle.bugmagic.client.render.entity.layer

import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.item.ItemWand
import com.deflatedpickle.bugmagic.common.util.Math
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.awt.ComponentOrientation
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class LayerCastingShape : LayerRenderer<EntityLivingBase> {
    private val tessellator = Tessellator.getInstance()

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    override fun doRenderLayer(entitylivingbaseIn: EntityLivingBase, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if (entitylivingbaseIn is EntityPlayerSP && entitylivingbaseIn.heldItemMainhand.item is ItemWand) {
            with(SpellLearner.isCapable(entitylivingbaseIn)) {
                if (this != null) {
                    with(this.spellList[this.currentIndex]) {
                        GlStateManager.pushMatrix()

                        GlStateManager.bindTexture(0)
                        GlStateManager.color(this.glowColour.red.toFloat(), this.glowColour.green.toFloat(), this.glowColour.blue.toFloat())

                        GlStateManager.translate(0f, entitylivingbaseIn.height - 0.4f, 0f)

                        for (tier in 1..this.tier.ordinal + 1) {
                            // TODO: Make the shapes pulsate, getting more frantic as the spell completes
                            val inner_radius = this.radius * (tier * Math.lerp(this.radiusMultiplier.left, this.radiusMultiplier.right, tier.toFloat()) / 3)
                            val isOdd = tier % 2 == 0
                            val time = (ageInTicks + partialTicks) * if (isOdd) -1f else 1f

                            GlStateManager.pushMatrix()
                            tessellator.buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION)

                            val size = sin(time / 8) * 0.1f
                            GlStateManager.scale(1f + size, 1f, 1f + size)

                            for (point in 0..this.castingShapePoints) {
                                val theta = (PI * 2.0) * point / this.castingShapePoints

                                val x = inner_radius * cos(theta)
                                val y = inner_radius * sin(theta)

                                val x2 = (inner_radius + this.castingShapeThickness) * cos(theta)
                                val y2 = (inner_radius + this.castingShapeThickness) * sin(theta)

                                // TODO: Make the points rotate between the spin speed ramp
                                GlStateManager.rotate(time / 14, 0f, inner_radius * 0.4f, 0f)
                                tessellator.buffer.pos(x, 0.0, y).endVertex()
                                tessellator.buffer.pos(x2, 0.1, y2).endVertex()
                            }

                            tessellator.draw()

                            GlStateManager.popMatrix()

                            if (!isOdd) {
                                for (i in listOf(-1, 1)) {
                                    GlStateManager.pushMatrix()
                                    tessellator.buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION)
                                    GlStateManager.rotate(time / 2 * tier * i, 0f, inner_radius * tier / 10, 0f)
                                    GlStateManager.scale(1f + size, 1f, 1f + size)

                                    val halfRadius = inner_radius / 2.6
                                    tessellator.buffer.pos(-halfRadius * 1.9, 0.0, -halfRadius * 1.9).endVertex()
                                    tessellator.buffer.pos(halfRadius * 1.9, 0.0, -halfRadius * 1.9).endVertex()
                                    tessellator.buffer.pos(0.0, 0.0, halfRadius * 2.8).endVertex()

                                    tessellator.draw()

                                    GlStateManager.popMatrix()
                                }
                            }
                        }

                        GlStateManager.popMatrix()
                    }
                }
            }
        }
    }

    private fun drawCircle(points: Int, inner_radius: Float, outer_radius: Float, time: Float) {
        GlStateManager.pushMatrix()
        tessellator.buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION)

        val size = sin(time / 8) * 0.1f
        GlStateManager.scale(1f + size, 1f, 1f + size)

        for (i in 0..points) {
            val theta = (PI * 2.0) * i / points

            val x = inner_radius * cos(theta)
            val y = inner_radius * sin(theta)

            val x2 = (inner_radius + outer_radius) * cos(theta)
            val y2 = (inner_radius + outer_radius) * sin(theta)

            // TODO: Make the points rotate between the spin speed ramp
            GlStateManager.rotate(time / 14, 0f, inner_radius * 0.4f, 0f)
            tessellator.buffer.pos(x, 0.0, y).endVertex()
            tessellator.buffer.pos(x2, 0.1, y2).endVertex()
        }

        tessellator.draw()

        GlStateManager.popMatrix()
    }
}