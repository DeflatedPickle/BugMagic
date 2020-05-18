/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.cout970.modelloader.api.Model
import com.cout970.modelloader.api.ModelLoaderApi
import com.cout970.modelloader.api.animation.IAnimatedModel
import com.cout970.modelloader.api.formats.gltf.GltfAnimationBuilder
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.IModelRegisterer
import com.deflatedpickle.bugmagic.api.IModelReloadListener
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorMob
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

/**
 * The renderer for [EssenceCollectorMob]
 */
class EssenceCollectorRender(renderManager: RenderManager) : RenderLiving<EssenceCollectorMob>(
        renderManager,
        object : ModelBase() {},
        0.5f
) {
    companion object : IModelRegisterer, IModelReloadListener {
        var models: HashMap<String, IAnimatedModel> = HashMap()

        override fun registerModels() {
            val modelID = ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "essence_collector"), "")
            val modelLocation = ResourceLocation(Reference.MOD_ID, "models/entity/essence collector/essence_collector.gltf")

            ModelLoaderApi.registerModel(modelID, modelLocation, false)
        }

        override fun reloadModels() {
            val modelID = ModelResourceLocation(Reference.MOD_ID + ":essence_collector", "")
            val entry = ModelLoaderApi.getModelEntry(modelID)

            val raw = entry?.raw

            if (raw != null && raw is Model.Gltf) {
                val data = raw.data
                val builder = GltfAnimationBuilder()
                builder.useTextureAtlas

                models.clear()
                builder.build(data).forEach { pair -> models[pair.first] = pair.second }
            }
        }
    }

    override fun getEntityTexture(entity: EssenceCollectorMob): ResourceLocation? {
        return null
    }

    override fun doRender(entity: EssenceCollectorMob, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        val time = (entity.ticksExisted and 0xFFFFFF).toDouble() + partialTicks
        models["Idle"]!!.render(time)

        GlStateManager.popMatrix()
    }
}
