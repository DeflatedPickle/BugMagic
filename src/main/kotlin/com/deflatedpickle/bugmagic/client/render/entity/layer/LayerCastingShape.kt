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

                        for (i in 1..this.tier.ordinal + 1) {
                            // TODO: Make the shapes pulsate, getting more frantic as the spell completes
                            drawCircle(
                                    this.castingShapePoints,
                                    this.radius * (i * Math.lerp(this.radiusMultiplier.left, this.radiusMultiplier.right, i.toFloat()) / 3),
                                    this.castingShapeThickness,
                                    (ageInTicks + partialTicks) * if (i % 2 == 0) -1f else 1f
                            )
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

        for (i in 0..points) {
            val theta = (PI * 2.0) * i / points

            val x = inner_radius * cos(theta)
            val y = inner_radius * sin(theta)

            val x2 = (inner_radius + outer_radius) * cos(theta)
            val y2 = (inner_radius + outer_radius) * sin(theta)

            // TODO: Make the points rotate between the spin speed ramp
            GlStateManager.rotate(time / 14, 0f, 1f, 0f)
            tessellator.buffer.pos(x, 0.0, y).endVertex()
            tessellator.buffer.pos(x2, 0.1, y2).endVertex()
        }

        tessellator.draw()

        GlStateManager.popMatrix()
    }
}