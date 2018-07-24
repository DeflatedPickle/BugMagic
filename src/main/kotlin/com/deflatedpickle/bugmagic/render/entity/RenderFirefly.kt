package com.deflatedpickle.bugmagic.render.entity

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.models.ModelFirefly
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.entity.RenderManager



class RenderFirefly(renderManager: RenderManager) : RenderLiving<EntityFirefly>(renderManager, ModelFirefly.model, 0f) {
    override fun getEntityTexture(entity: EntityFirefly?): ResourceLocation? {
        // TODO: Fix texture
        return ResourceLocation("assets/bugmagic/textures/entities/firefly")
    }
}