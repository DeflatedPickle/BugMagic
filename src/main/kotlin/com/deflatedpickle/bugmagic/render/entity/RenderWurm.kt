package com.deflatedpickle.bugmagic.render.entity

import com.deflatedpickle.bugmagic.entity.mob.EntityWurm
import com.deflatedpickle.bugmagic.models.ModelWurm
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.entity.RenderManager

class RenderWurm(renderManager: RenderManager) : RenderLiving<EntityWurm>(renderManager, ModelWurm.model, 0f) {
    val texture = ResourceLocation("bugmagic:textures/entity/wurm.png")

    override fun getEntityTexture(entity: EntityWurm): ResourceLocation? {
        return texture
    }
}