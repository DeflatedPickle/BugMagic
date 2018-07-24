package com.deflatedpickle.bugmagic.render.entity

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.models.ModelFirefly
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.entity.RenderManager
import com.deflatedpickle.bugmagic.render.entity.layers.LayerFireflyLight


class RenderFirefly(renderManager: RenderManager) : RenderLiving<EntityFirefly>(renderManager, ModelFirefly.model, 0f) {
    val texture = ResourceLocation("bugmagic:textures/entity/firefly/firefly.png")

    init {
        this.addLayer(LayerFireflyLight(this))
    }

    override fun getEntityTexture(entity: EntityFirefly?): ResourceLocation? {
        return texture
    }
}