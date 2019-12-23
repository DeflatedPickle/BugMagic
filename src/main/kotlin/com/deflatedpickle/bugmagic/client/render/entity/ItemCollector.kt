/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.cout970.modelloader.api.Model
import com.cout970.modelloader.api.ModelLoaderApi
import com.cout970.modelloader.api.animation.IAnimatedModel
import com.cout970.modelloader.api.formats.gltf.GltfAnimationBuilder
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.IModelRegisterer
import com.deflatedpickle.bugmagic.api.IModelReloadListener
import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector as Mob
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import org.lwjgl.util.ReadableColor

class ItemCollector(renderManager: RenderManager) : RenderCastable<Mob>(renderManager, 0f) {
    companion object : IModelRegisterer, IModelReloadListener {
        val axisAlignedBB = AxisAlignedBB(-6.0, 0.1, -6.0, 7.0, 1.0, 7.0)

        var models: HashMap<String, IAnimatedModel> = HashMap()

        override fun registerModels() {
            val modelID = ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "item_collector"), "")
            val modelLocation = ResourceLocation(Reference.MOD_ID, "models/entity/item collector/item_collector.gltf")

            ModelLoaderApi.registerModel(modelID, modelLocation, false)
        }

        override fun reloadModels() {
            val modelID = ModelResourceLocation(Reference.MOD_ID + ":item_collector", "")
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

    override fun getEntityTexture(entity: Mob): ResourceLocation? {
        return null
    }

    override fun doRender(entity: Mob, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()

        if (entity.owner?.heldItemMainhand?.item is Wand) {
            val blockPos = entity.dataManager.get(Mob.dataInventoryPosition)
            drawWorkArea(
                    axisAlignedBB,
                    blockPos,
                    Minecraft.getMinecraft().player,
                    partialTicks
            )
        }

        GlStateManager.translate(x, y, z)

        if (entity.owner?.heldItemMainhand?.item is Wand) {
            drawInventoryLine(
                    entity,
                    entity.dataManager.get(Mob.dataInventoryPosition),
                    ReadableColor.RED
            )
        }

        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        drawItem(entity, entity.dataManager.get(Mob.dataItemStack))

        val time = (entity.ticksExisted and 0xFFFFFF).toDouble() + partialTicks
        models["Idle"]!!.render(time)

        GlStateManager.popMatrix()
    }
}
