package com.deflatedpickle.bugmagic.render.tileentity

import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.bugmagic.util.TextureUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class RenderCauldron : TileEntitySpecialRenderer<TileEntityCauldron>() {
    private val textureWater = ResourceLocation("minecraft:textures/blocks/water_still.png")
    private val texturePlanks = ResourceLocation("minecraft:textures/blocks/planks_oak.png")

    private val newTextureWater = TextureUtil.recolourTexture(textureWater, textureWater.resourceDomain + "_recolour", "#BBFF70")

    private val tessellator = Tessellator.getInstance()

    val size = 0.03125

    override fun render(te: TileEntityCauldron, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        drawFluidLayer(x, y, z)
        drawHandle(x, y, z)
    }

    private fun drawFluidLayer(x: Double, y: Double, z: Double) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)

        Minecraft.getMinecraft().textureManager.bindTexture(newTextureWater)
        GL11.glDisable(GL11.GL_LIGHTING)

        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

        // TODO: Animate the liquid

        // TODO: Render the height of the liquid
        // TODO: Remove the stick from the model
        // TODO: Render the stick in a matrix here, if the cauldron has one
        // TODO: Render the right liquid type

        // Bottom Left
        tessellator.buffer.pos(0.9, 0.95, 0.1).tex(0.0, 0.0).endVertex()
        // Bottom Right
        tessellator.buffer.pos(0.1, 0.95, 0.1).tex(0.0, size).endVertex()
        // Top Right
        tessellator.buffer.pos(0.1, 0.95, 0.9).tex(1.0, size).endVertex()
        // Top Left
        tessellator.buffer.pos(0.9, 0.95, 0.9).tex(1.0, 0.0).endVertex()

        tessellator.draw()

        GlStateManager.popMatrix()
    }

    private fun drawHandle(x: Double, y: Double, z: Double) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.32, 0.5, 0.6)
        GlStateManager.rotate(25f, 1f, 0f, 0f)

        GlStateManager.translate(0.0, -0.35, -0.2)

        Minecraft.getMinecraft().textureManager.bindTexture(texturePlanks)
        GL11.glDisable(GL11.GL_LIGHTING)

        val heightBottom = 0.0
        val heightTop = 1.2

        val size = 0.24
        val texSize = 0.125

        // Top Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(size, heightTop, 0.1).tex(texSize, 0.0).endVertex()
            // Bottom Right
        tessellator.buffer.pos(0.1, heightTop, 0.1).tex(0.0, 0.0).endVertex()
            // Top Right
        tessellator.buffer.pos(0.1, heightTop, size).tex(0.0, texSize).endVertex()
            // Top Left
        tessellator.buffer.pos(size, heightTop, size).tex(texSize, texSize).endVertex()
        tessellator.draw()

        // Bottom Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(size, heightBottom, 0.1).tex(texSize, 0.0).endVertex()
            // Bottom Right
        tessellator.buffer.pos(0.1, heightBottom, 0.1).tex(0.0, 0.0).endVertex()
            // Top Right
        tessellator.buffer.pos(0.1, heightBottom, size).tex(0.0, texSize).endVertex()
            // Top Left
        tessellator.buffer.pos(size, heightBottom, size).tex(texSize, texSize).endVertex()
        tessellator.draw()

        val sizeSide = size
        val texWidth = 0.125
        val texHeight = 1.0

        // TODO: Map each face to a different part of the texture

        // Front Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(sizeSide, heightBottom, 0.1).tex(0.0, texHeight).endVertex()
            // Bottom Right
        tessellator.buffer.pos(0.1, heightBottom, 0.1).tex(texWidth, texHeight).endVertex()
            // Top Right
        tessellator.buffer.pos(0.1, heightTop, 0.1).tex(texWidth, 0.0).endVertex()
            // Top Left
        tessellator.buffer.pos(sizeSide, heightTop, 0.1).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        // Back Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(0.1, heightBottom, size).tex(0.0, texHeight).endVertex()
            // Bottom Right
        tessellator.buffer.pos(sizeSide, heightBottom, size).tex(texWidth, texHeight).endVertex()
            // Top Right
        tessellator.buffer.pos(sizeSide, heightTop, size).tex(texWidth, 0.0).endVertex()
            // Top Left
        tessellator.buffer.pos(0.1, heightTop, size).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        // Right Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(0.1, heightBottom, 0.1).tex(0.0, texHeight).endVertex()
            // Bottom Right
        tessellator.buffer.pos(0.1, heightBottom, size).tex(texWidth, texHeight).endVertex()
            // Top Right
        tessellator.buffer.pos(0.1, heightTop, size).tex(texWidth, 0.0).endVertex()
            // Top Left
        tessellator.buffer.pos(0.1, heightTop, 0.1).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        // Left Face
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            // Bottom Left
        tessellator.buffer.pos(size, heightBottom, size).tex(0.0, texHeight).endVertex()
            // Bottom Right
        tessellator.buffer.pos(size, heightBottom, 0.1).tex(texWidth, texHeight).endVertex()
            // Top Right
        tessellator.buffer.pos(size, heightTop, 0.1).tex(texWidth, 0.0).endVertex()
            // Top Left
        tessellator.buffer.pos(size, heightTop, size).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        GlStateManager.popMatrix()
    }
}