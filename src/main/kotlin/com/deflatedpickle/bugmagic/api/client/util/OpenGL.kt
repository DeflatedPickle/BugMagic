/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11

object OpenGL {
    val tessellator = Tessellator.getInstance()

    fun drawSelectionBox(aabb: AxisAlignedBB, blockPos: BlockPos, player: EntityPlayer, partialTicks: Float) {
        // Copied from RenderGlobal#drawSelectionBox
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        GlStateManager.glLineWidth(2.0f)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)

        val d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
        val d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
        val d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks

        RenderGlobal.drawSelectionBoundingBox(aabb
                .offset(blockPos)
                .grow(0.0020000000949949026)
                .offset(-d3, -d4, -d5),
                0f, 0f, 0f, 0.5f)

        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

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
