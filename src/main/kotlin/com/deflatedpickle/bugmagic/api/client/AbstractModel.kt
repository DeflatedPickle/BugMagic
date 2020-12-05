/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client

import com.cout970.modelloader.api.Model
import com.cout970.modelloader.api.ModelLoaderApi
import com.cout970.modelloader.api.animation.IAnimatedModel
import com.cout970.modelloader.api.formats.gltf.GltfAnimationBuilder
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.IModelRegisterer
import com.deflatedpickle.bugmagic.api.IModelReloadListener
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation

abstract class AbstractModel(private val path: String) : IModelRegisterer, IModelReloadListener {
    var models: HashMap<String, IAnimatedModel> = HashMap()

    override fun registerModels() {
        val modelID = ModelResourceLocation(ResourceLocation(Reference.MOD_ID, path), "")
        val modelLocation = ResourceLocation(Reference.MOD_ID, "models/entity/$path/$path.gltf")

        ModelLoaderApi.registerModel(modelID, modelLocation, false)
    }

    override fun reloadModels() {
        val modelID = ModelResourceLocation("${Reference.MOD_ID}:$path", "")
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
