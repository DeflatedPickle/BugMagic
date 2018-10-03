package com.deflatedpickle.bugmagic.render.tileentity

import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.bugmagic.util.TextureUtil
import com.deflatedpickle.picklelib.colour.Colour
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.net.URL
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class RenderCauldron : TileEntitySpecialRenderer<TileEntityCauldron>() {
    private val texture = ResourceLocation("minecraft:textures/blocks/water_still.png")

    private val newTexture = TextureUtil.recolourTexture(texture, texture.resourceDomain + "_recolour", "#BBFF70")

    private val tessellator = Tessellator.getInstance()

    override fun render(te: TileEntityCauldron, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)

        Minecraft.getMinecraft().textureManager.bindTexture(newTexture)

        GL11.glDisable(GL11.GL_LIGHTING)

        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

        val size = 0.03125

        // TODO: Animate the liquid

        // Bottom Right
        tessellator.buffer.pos(0.9, 0.95, 0.1).tex(0.0, 0.0).endVertex()

        // Bottom Left
        tessellator.buffer.pos(0.1, 0.95, 0.1).tex(0.0, size).endVertex()

        // Top Left
        tessellator.buffer.pos(0.1, 0.95, 0.9).tex(1.0, size).endVertex()

        // Top Right
        tessellator.buffer.pos(0.9, 0.95, 0.9).tex(1.0, 0.0).endVertex()

        tessellator.draw()

        GlStateManager.popMatrix()
    }
}