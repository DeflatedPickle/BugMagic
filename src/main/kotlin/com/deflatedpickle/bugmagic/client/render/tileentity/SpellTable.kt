package com.deflatedpickle.bugmagic.client.render.tileentity

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ForgeHooksClient
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpellTable : TileEntitySpecialRenderer<SpellTableTE>() {
    override fun render(te: SpellTableTE, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 140f, 140f)
        RenderHelper.enableStandardItemLighting()

        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1, z + 0.5)

        val itemCount = 0.until(te.itemStackHandler.slots).count { te.itemStackHandler.getStackInSlot(it) != ItemStack.EMPTY }

        GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 0.4f, 0f)
        GlStateManager.pushMatrix()

        for (i in 0 until te.itemStackHandler.slots) {
            val stack = te.itemStackHandler.getStackInSlot(i)

            if (stack != ItemStack.EMPTY) {
                GlStateManager.pushMatrix()

                val theta = (PI * 2) * i / itemCount

                val radius1 = 1.2
                val x1 = radius1 * cos(theta)
                val z1 = radius1 * sin(theta)

                GlStateManager.translate(x1, sin(te.world.totalWorldTime.toFloat() * 0.1f) * 0.06, z1)
                GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 1.2f, 0f)

                for (j in 0 until stack.count) {
                    GlStateManager.pushMatrix()

                    val theta2 = (PI * 2) * j / stack.count

                    val radius2 = 0.4
                    val x2 = radius2 * cos(theta2)
                    val z2 = radius2 * sin(theta2)

                    val speed = 0.2
                    val amplitude = 0.05
                    GlStateManager.translate(x2, sin(((te.world.totalWorldTime.toFloat() + j) * (PI / 2)) * amplitude) * speed, z2)

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