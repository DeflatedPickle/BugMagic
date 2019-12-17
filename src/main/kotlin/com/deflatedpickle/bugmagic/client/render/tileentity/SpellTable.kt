/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.tileentity

import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ForgeHooksClient

class SpellTable : TileEntitySpecialRenderer<SpellTableTE>() {
    override fun render(te: SpellTableTE, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 140f, 140f)
        RenderHelper.enableStandardItemLighting()

        // Eye
        // TODO: Make the eye look at the player's eyes
        GlStateManager.pushMatrix()
        val eyeSpeed = 0.02
        val eyeAmplitude = 0.02
        GlStateManager.translate(x + 0.8, y + 0.15 + sin(te.world.totalWorldTime.toFloat() * eyeSpeed) * eyeAmplitude, z + 0.45)
        GlStateManager.rotate(45f, 0f, -1.5f, 1f)
        GlStateManager.scale(0.6f, 0.6f, 0.6f)

        val stack = ItemStack(Items.SPIDER_EYE)
        var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, this.world, null)
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().renderItem.renderItem(stack, model)

        GlStateManager.popMatrix()

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
