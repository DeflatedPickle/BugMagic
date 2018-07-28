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
}