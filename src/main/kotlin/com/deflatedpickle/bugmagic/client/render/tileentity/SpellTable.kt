/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.tileentity

import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import com.deflatedpickle.bugmagic.common.item.Wand
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11

class SpellTable : TileEntitySpecialRenderer<SpellTableTE>() {
    val tessellator = Tessellator.getInstance()

    override fun render(te: SpellTableTE, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        fun rectangle(width: Double, depth: Double, lu: Float, lv: Float, mu: Float, mv: Float) {
            tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            arrayOf(lu, lv, mu, mv).map { it.toDouble() }.apply {
                // Bottom Left
                tessellator.buffer.pos(width, 0.0, 0.0).tex(this[0], this[1]).endVertex()
                // Bottom Right
                tessellator.buffer.pos(0.0, 0.0, 0.0).tex(this[0], this[3]).endVertex()
                // Top Right
                tessellator.buffer.pos(0.0, 0.0, depth).tex(this[2], this[3]).endVertex()
                // Top Left
                tessellator.buffer.pos(width, 0.0, depth).tex(this[2], this[1]).endVertex()
            }
            tessellator.draw()
        }

        fun cube(width: Double, depth: Double, height: Double, lu: Float, lv: Float, mu: Float, mv: Float) {
            GlStateManager.pushMatrix()
            rectangle(width, depth, lu, lv, mu, mv)

            GlStateManager.translate(0.0, height, 0.0)
            rectangle(width, depth, lu, lv, mu, mv)

            GlStateManager.pushMatrix()
            GlStateManager.rotate(-90f, 0f, 0f, 1f)
            rectangle(0.4, depth, lu, lv, mu, mv)

            GlStateManager.translate(0.0, width, 0.0)
            rectangle(0.4, depth, lu, lv, mu, mv)
            GlStateManager.popMatrix()

            GlStateManager.rotate(90f, 1f, 0f, 0f)
            rectangle(width, 0.4, lu, lv, mu, mv)

            GlStateManager.translate(0.0, depth, 0.0)
            rectangle(width, 0.4, lu, lv, mu, mv)
            GlStateManager.popMatrix()
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 140f, 140f)
        RenderHelper.enableStandardItemLighting()

        // Fluid
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 1.135, y + 0.515, z + 0.57)
        GlStateManager.rotate(22.5f, 0f, 0f, 1f)

        te.fluidTank.fluid?.fluid?.still?.let {
            GlStateManager.disableCull()
            val sprite = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(it.toString())
            Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

            cube(0.1, 0.18, (0.4 * (te.fluidTank.fluidAmount / 1000)), sprite.minU, sprite.minV, sprite.maxU, sprite.maxV)
        }

        GlStateManager.enableCull()
        GlStateManager.popMatrix()

        // Wand
        val stack = te.wandStackHandler.getStackInSlot(0)
        if (stack.item is Wand && stack.count > 0) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x + 1.1, y + 0.6, z + 0.3)
            GlStateManager.rotate(60f, 0.2f, -0.8f, 1f)

            var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, this.world, null)
            model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

            Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            Minecraft.getMinecraft().renderItem.renderItem(stack, model)

            GlStateManager.popMatrix()
        }

        // Eye
        // TODO: Make the eye look at the player's eyes
        GlStateManager.pushMatrix()
        val eyeSpeed = 0.02
        val eyeAmplitude = 0.02
        GlStateManager.translate(x + 0.8, y + 0.15 + sin(te.world.totalWorldTime.toFloat() * eyeSpeed) * eyeAmplitude, z + 0.45)
        GlStateManager.rotate(45f, 0f, -1.5f, 1f)
        GlStateManager.scale(0.6f, 0.6f, 0.6f)

        val eyeStack = ItemStack(Items.SPIDER_EYE)
        var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(eyeStack, this.world, null)
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().renderItem.renderItem(eyeStack, model)

        GlStateManager.popMatrix()

        // Item ring
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1, z + 0.5)

        val itemCount = 0.until(te.itemStackHandler.slots).count { te.itemStackHandler.getStackInSlot(it) != ItemStack.EMPTY }

        GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 0.4f, 0f)
        GlStateManager.pushMatrix()

        for (i in 0 until te.itemStackHandler.slots) {
            val stack = te.itemStackHandler.getStackInSlot(i)

            // Renders all non-empty stacks in a circle
            if (stack != ItemStack.EMPTY) {
                GlStateManager.pushMatrix()

                val bigTheta = (PI * 2) * i / itemCount

                val bigRadius = 1.2
                val bigX = bigRadius * cos(bigTheta)
                val bigZ = bigRadius * sin(bigTheta)

                val bigSpeed = 0.1f
                val bigAmplitude = 0.06
                GlStateManager.translate(bigX, sin(te.world.totalWorldTime.toFloat() * bigSpeed) * bigAmplitude, bigZ)
                GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 1.2f, 0f)

                // Renders all items in the stack in a smaller circle
                for (j in 0 until stack.count) {
                    GlStateManager.pushMatrix()

                    val smallTheta = (PI * 2) * j / stack.count

                    val smallRadius = 0.4
                    val smallX = smallRadius * cos(smallTheta)
                    val smallZ = smallRadius * sin(smallTheta)

                    val smallSpeed = 0.05
                    val smallAmplitude = 0.2f
                    GlStateManager.translate(smallX, sin(((te.world.totalWorldTime.toFloat() + j) * (PI / 2)) * smallSpeed) * smallAmplitude, smallZ)

                    var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, this.world, null)
                    model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

                    Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    Minecraft.getMinecraft().renderItem.renderItem(stack, model)

                    GlStateManager.popMatrix()
                }

                GlStateManager.popMatrix()
            }
        }

        GlStateManager.popMatrix()
        GlStateManager.popMatrix()

        RenderHelper.disableStandardItemLighting()
    }
}
