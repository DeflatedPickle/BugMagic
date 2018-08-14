package com.deflatedpickle.bugmagic.render.entity.layers

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.render.entity.RenderFirefly
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.util.ResourceLocation

class LayerFireflyLight(private val fireflyRendererIn: RenderFirefly) : LayerRenderer<EntityFirefly> {
    val texture = ResourceLocation("bugmagic:textures/entity/firefly/firefly_light.png")

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    override fun doRenderLayer(entitylivingbaseIn: EntityFirefly?, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        this.fireflyRendererIn.bindTexture(texture)

        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()

        GlStateManager.disableLighting()
        GlStateManager.depthMask(!entitylivingbaseIn!!.isInvisible)
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f)
        GlStateManager.enableLighting()
        GlStateManager.color(1f, 0.9f, 0.3f, 1.0f)

        this.fireflyRendererIn.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        this.fireflyRendererIn.setLightmap(entitylivingbaseIn)

        GlStateManager.depthMask(true)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
    }
}