package com.deflatedpickle.bugmagic.client.render.entity

import com.cout970.modelloader.api.Model
import com.cout970.modelloader.api.ModelLoaderApi
import com.cout970.modelloader.api.animation.IAnimatedModel
import com.cout970.modelloader.api.formats.gltf.GltfAnimationBuilder
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.IModelRegisterer
import com.deflatedpickle.bugmagic.api.IModelReloadListener
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector as Mob
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11

class ItemCollector(renderManager: RenderManager) : RenderLiving<Mob>(renderManager, object : ModelBase() {}, 0f) {
    companion object : IModelRegisterer, IModelReloadListener {
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

    private val tessellator = Tessellator.getInstance()

    override fun getEntityTexture(entity: Mob): ResourceLocation? {
        return null
    }

    override fun doRender(entity: Mob, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x , y, z)

        if (entity.owner?.heldItemMainhand?.item is Wand) {
            drawInventoryLine(entity)
        }

        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        drawItem(entity)

        val time = (entity.ticksExisted and 0xFFFFFF).toDouble() + partialTicks
        models["Idle"]!!.render(time)

        GlStateManager.popMatrix()
    }

    private fun drawInventoryLine(entity: Mob) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT)
        GlStateManager.disableTexture2D()
        GlStateManager.color(1f, 0f, 0f, 1f)
        tessellator.buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        tessellator.buffer.pos(0.0, 0.0, 0.0).endVertex()
        entity.dataManager.get(Mob.dataInventoryPosition).also {
            tessellator.buffer.pos(it.x - entity.posX + 0.5, it.y - entity.posY + 0.5, it.z - entity.posZ + 0.5).endVertex()
        }
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GL11.glPopAttrib()
    }

    fun drawItem(entity: Mob) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(0f, 0.2f, 0f)

        val stack = entity.dataManager.get(Mob.dataItemStack)

        var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, entity.world, null)
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().renderItem.renderItem(stack, model)

        GlStateManager.popMatrix()
    }
}