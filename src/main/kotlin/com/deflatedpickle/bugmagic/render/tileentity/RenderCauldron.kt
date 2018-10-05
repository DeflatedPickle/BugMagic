package com.deflatedpickle.bugmagic.render.tileentity

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.bugmagic.util.TextureUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Tuple
import net.minecraftforge.client.ForgeHooksClient
import org.apache.commons.lang3.RandomUtils
import org.lwjgl.opengl.GL11

class RenderCauldron : TileEntitySpecialRenderer<TileEntityCauldron>() {
    private val textureWater = ResourceLocation("minecraft:textures/blocks/water_still.png")
    private val texturePlanks = ResourceLocation("minecraft:textures/blocks/planks_oak.png")
    private val textureBugEssence = TextureUtil.recolourTexture(textureWater, textureWater.resourceDomain + "_recolour", "#BBFF70")

    private val tessellator = Tessellator.getInstance()

    val size = 0.03125

    var waterHeight = 0.0
    // TODO: Move the animation variables to the TileEntity
    private var angle = 0f

    private var stirAmount = 0.0

    private var wasStirred = false
    private var stirRotationAmount = 0.0  // Speed
    // TODO: Apply more drag as it becomes more bug essence
    private val stirRotationDrag = 0.55
    private val stirEmptyRotationDrag = 2.0
    private val stirRotationMomentum = 0.1

    private var stirCurrentDrag = stirRotationDrag

    private var stirRotationMomentumTick = 0

    private val stirRotationMin = 10
    private val stirRotationMax = 13

    private val partRotationList = mutableListOf<Float>()
    private val partPositionList = mutableListOf<Tuple<Double, Double>>()
    private val partSizeList = mutableListOf<Float>()

    override fun render(te: TileEntityCauldron, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        waterHeight = 0.15 + (0.95 - 0.15) * te.waterAmount

        for (i in 0 until te.getPartAmount()) {
            //if (i > 0) {
                // TODO: Make items slowly bob up and down in the water
                // TODO: Rotate the items locally based on a fraction of momentum gained from spinning the stick

                if (partRotationList.getOrNull(i) == null) {
                    partRotationList.add(RandomUtils.nextFloat(0f, 360f))
                }

                if (partPositionList.getOrNull(i) == null) {
                    val min = -0.1
                    val max = 0.1

                    partPositionList.add(Tuple(Math.random() * (max - min) + min, Math.random() * (max - min) + min))
                }

                if (partSizeList.getOrNull(i) == null) {
                    partSizeList.add(RandomUtils.nextFloat(0.5f, 0.7f))
                }

                drawItem(x, y, z, ItemStack(ModItems.bugParts[te.getParts()[i]]), i, te.getPartAmount(), te.stirAmount, te.stirsRequired)
            //}
        }

        if (te.hasStirrer) {
            drawHandle(x, y, z, te.waterAmount, te.stirAmount)
        }

        if (te.waterAmount >= 0.0f) {
            // drawFluidLayer(x, y, z, te.waterAmount, te.stirAmount)
            drawFluid(x, y, z, te.waterAmount, te.stirAmount, te.stirsRequired)
        }
    }

    private fun drawFluid(x: Double, y: Double, z: Double, fluidHeight: Float, stirAmount: Double, stirsRequired: Double) {
        GlStateManager.pushMatrix()

        // TODO: Check if the cauldron has bug parts, if so, do this

        val stirsNeeded = if (stirsRequired == 0.0 && stirAmount == 0.0) {
            15 / stirAmount.toFloat()
        }
        else {
            (stirsRequired / stirAmount).toFloat()
        }

        GlStateManager.color(1f, 1f, 1f, stirsNeeded)
        drawFluidLayer(x, y, z, textureWater, fluidHeight)

        GlStateManager.color(1f, 1f, 1f, (stirAmount / stirsRequired).toFloat())
        drawFluidLayer(x, y, z, textureBugEssence, fluidHeight)

        GlStateManager.popMatrix()
    }

    private fun drawFluidLayer(x: Double, y: Double, z: Double, texture: ResourceLocation, fluidHeight: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        GlStateManager.translate(x, y, z)

        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        GL11.glDisable(GL11.GL_LIGHTING)

        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

        // TODO: Animate the liquid
        // TODO: Make the water spin with the stirrer by rotating the UVs
        // TODO: Puff out particles when stirring

        // Bottom Left
        tessellator.buffer.pos(0.9, waterHeight, 0.1).tex(0.0, 0.0).endVertex()
        // Bottom Right
        tessellator.buffer.pos(0.1, waterHeight, 0.1).tex(0.0, size).endVertex()
        // Top Right
        tessellator.buffer.pos(0.1, waterHeight, 0.9).tex(1.0, size).endVertex()
        // Top Left
        tessellator.buffer.pos(0.9, waterHeight, 0.9).tex(1.0, 0.0).endVertex()

        tessellator.draw()

        GL11.glEnable(GL11.GL_LIGHTING)
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    private fun drawItem(x: Double, y: Double, z: Double, item: ItemStack, partOrder: Int, partCount: Int, stirAmount: Double, stirsRequired: Double) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)

        val offset = 0.5
        GlStateManager.translate(offset, 0.0, offset)
        // TODO: Add an rotation handler (like the handle has) to apply more drag the lower the part is
        GlStateManager.rotate(angle - partRotationList[partOrder] - ((partOrder - partCount) * 10), 0f, 1f, 0f)
        GlStateManager.translate(-offset, 0.0, -offset)

        // TODO: Properly stack the items when there's not enough water to make them float
        var height = (waterHeight - 0.001) - (0.05 * partOrder) * partCount

        if (height < 0.2) {
            height = 0.2
        }

        GlStateManager.translate(0.36 - partPositionList[partOrder].first,
                height,
                0.36 - partPositionList[partOrder].second)

        GlStateManager.rotate(90f, 1f, 0f, 0f)

        // TODO: Make the items more transparent as they are mixed in
        var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(item, Minecraft.getMinecraft().world, null)
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

        GlStateManager.scale(partSizeList[partOrder], partSizeList[partOrder], partSizeList[partOrder])

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().renderItem.renderItem(item, model)

        GlStateManager.popMatrix()
    }

    private fun drawHandle(x: Double, y: Double, z: Double, fluidHeight: Float, tileStirAmount: Double) {
        // Check if it was stirred
        if (stirAmount < tileStirAmount) {
            wasStirred = true
            stirAmount = tileStirAmount
        }

        if (fluidHeight < 0.5f) {
            stirCurrentDrag = 0.25 / fluidHeight
        }
        else if (fluidHeight > 0.5f) {
            stirCurrentDrag = 0.25 * fluidHeight
        }

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)

        // Stir the stick
        if (wasStirred) {
            stirRotationAmount = RandomUtils.nextInt(stirRotationMin, stirRotationMax).toDouble()

            wasStirred = false

            stirRotationMomentumTick = 4
        }

        if (stirRotationMomentumTick > 0) {
            stirRotationMomentumTick -= 1

            stirRotationAmount += stirRotationMomentum
        }

        GlStateManager.translate(0.5, 0.5, 0.5)
        GlStateManager.rotate(angle, 0f, 1f, 0f)
        GlStateManager.translate(-0.5, -0.5, -0.5)

        if (stirRotationAmount > 0.1) {
            stirRotationAmount -= stirCurrentDrag
        }
        else {
            stirRotationAmount = 0.0
        }
        angle += stirRotationAmount.toFloat()

        // Tilt the stick
        // TODO: Reset the stick to 25f when not stirred for awhile
        GlStateManager.rotate(17f, 1f, 0f, 0f)
        GlStateManager.translate(0.0, -0.35, -0.2)

        // Move the stick to the center
        GlStateManager.translate(0.32, 0.5, 0.5)

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
        tessellator.buffer.pos(0.1, heightBottom, 0.1).tex(texSize, 0.0).endVertex()
            // Bottom Right
        tessellator.buffer.pos(size, heightBottom, 0.1).tex(0.0, 0.0).endVertex()
            // Top Right
        tessellator.buffer.pos(size, heightBottom, size).tex(0.0, texSize).endVertex()
            // Top Left
        tessellator.buffer.pos(0.1, heightBottom, size).tex(texSize, texSize).endVertex()
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

        GL11.glEnable(GL11.GL_LIGHTING)
        GlStateManager.popMatrix()
    }
}