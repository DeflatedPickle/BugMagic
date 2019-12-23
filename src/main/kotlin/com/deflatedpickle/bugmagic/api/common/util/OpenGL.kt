/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util

import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

object OpenGL {
    val tessellator = Tessellator.getInstance()

    fun cube(width: Double, height: Double, depth: Double, lu: Float, lv: Float, mu: Float, mv: Float) {
        arrayOf(lu, lv, mu, mv).map { it.toDouble() }.apply {
            // Top Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(width, height, 0.0).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(0.0, height, 0.0).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(0.0, height, depth).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(width, height, depth).tex(this[2], this[1]).endVertex()
            tessellator.draw()

            // Bottom Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(0.0, 0.0, 0.0).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(width, 0.0, 0.0).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(width, 0.0, depth).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(0.0, 0.0, depth).tex(this[2], this[1]).endVertex()
            tessellator.draw()

            // Front Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(width, 0.0, 0.0).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(0.0, 0.0, 0.0).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(0.0, height, 0.0).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(width, height, 0.0).tex(this[2], this[1]).endVertex()
            tessellator.draw()

            // Back Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(0.0, 0.0, depth).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(width, 0.0, depth).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(width, height, depth).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(0.0, height, depth).tex(this[2], this[1]).endVertex()
            tessellator.draw()

            // Right Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(0.0, 0.0, 0.0).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(0.0, 0.0, depth).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(0.0, height, depth).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(0.0, height, 0.0).tex(this[2], this[1]).endVertex()
            tessellator.draw()

            // Left Face
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
            tessellator.buffer.pos(width, 0.0, depth).tex(this[0], this[1]).endVertex()
            // Bottom Right
            tessellator.buffer.pos(width, 0.0, 0.0).tex(this[0], this[3]).endVertex()
            // Top Right
            tessellator.buffer.pos(width, height, 0.0).tex(this[0], this[3]).endVertex()
            // Top Left
            tessellator.buffer.pos(width, height, depth).tex(this[2], this[1]).endVertex()
            tessellator.draw()
        }
    }
}
