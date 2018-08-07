package com.deflatedpickle.bugmagic.render.entity

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack
import com.deflatedpickle.bugmagic.models.ModelBugpack
import net.minecraft.client.model.ModelChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraftforge.common.DimensionManager.getWorld
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ForgeHooksClient
import net.minecraftforge.items.ItemStackHandler


class RenderBugpack(renderManager: RenderManager) : RenderLiving<EntityBugpack>(renderManager, ModelBugpack.model, 0.35f) {
    val texture = ResourceLocation("bugmagic:textures/entity/bugpack.png")

    override fun getEntityTexture(entity: EntityBugpack?): ResourceLocation? {
        return texture
    }

    override fun doRender(entity: EntityBugpack, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        val ny = y + 0.33f
        super.doRender(entity, x, ny, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        run {
            // Move the item to the bugs location
            GlStateManager.translate(x , ny + 0.07f, z)
            GlStateManager.rotate(-entityYaw, 0f, 1f, 0f)

            // // Rotate the item
            GlStateManager.rotate(180f, 1f, 0f, 0f)
            GlStateManager.rotate(45f, 0f, 0f, 1f)
            GlStateManager.scale(0.5f, 0.5f, 0.5f)

            GlStateManager.translate(0.13f, 0f, 0f)

            GlStateManager.rotate(15f, 1f, 0f, 0f)

            val stack = entity.dataManager.get(EntityBugpack.dataItemStack)

            var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, entity.world, null)
            model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

            Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            Minecraft.getMinecraft().renderItem.renderItem(stack, model)
        }
        GlStateManager.popMatrix()
    }
}