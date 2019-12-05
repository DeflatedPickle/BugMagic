/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.cout970.modelloader.api.animation.IAnimatedModel
import com.deflatedpickle.bugmagic.api.IModelRegisterer
import com.deflatedpickle.bugmagic.api.IModelReloadListener
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoe as Mob
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

class AutoHoe(renderManager: RenderManager) : RenderLiving<Mob>(renderManager, object : ModelBase() {}, 0.5f) {
    companion object : IModelRegisterer, IModelReloadListener {
        var models: HashMap<String, IAnimatedModel> = HashMap()

        override fun registerModels() {
        }

        override fun reloadModels() {
        }
    }

    override fun getEntityTexture(entity: Mob): ResourceLocation? {
        return null
    }

    override fun doRender(entity: Mob, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        GlStateManager.popMatrix()
    }
}
