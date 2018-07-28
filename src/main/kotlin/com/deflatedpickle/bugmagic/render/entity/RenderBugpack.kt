package com.deflatedpickle.bugmagic.render.entity

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack
import com.deflatedpickle.bugmagic.models.ModelBugpack
import net.minecraft.client.model.ModelChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.entity.RenderManager


class RenderBugpack(renderManager: RenderManager) : RenderLiving<EntityBugpack>(renderManager, ModelBugpack.model, 0.35f) {
    val texture = ResourceLocation("bugmagic:textures/entity/bugpack.png")

    val chest_model = ModelChest()
    val chest_texture = ResourceLocation("textures/entity/chest/normal.png")

    override fun getEntityTexture(entity: EntityBugpack?): ResourceLocation? {
        return texture
    }

    override fun doRender(entity: EntityBugpack, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        val ny = y + 0.33f
        super.doRender(entity, x, ny, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        run {
            // Move the chest to the bugs location
            GlStateManager.translate(x, ny, z)
            GlStateManager.rotate(-entityYaw, 0f, 1f, 0f)

            // Rotate the chest to stand "up"
            GlStateManager.rotate(180f, 1f, 0f, 0f)
            GlStateManager.scale(0.35f, 0.35f, 0.35f)

            GlStateManager.translate(-0.5f, -0.3f, -0.45f)
            GlStateManager.rotate(15f, 1f, 0f, 0f)
            GlStateManager.translate(0.005f, 0.2f, -0.1f)

            this.bindTexture(chest_texture)
            chest_model.renderAll()
        }
        GlStateManager.popMatrix()
    }
}