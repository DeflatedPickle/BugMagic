/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util.extension

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.ForgeHooksClient

fun ItemStack.render(world: World) {
    var model = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(this, world, null)
    model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false)

    Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    Minecraft.getMinecraft().renderItem.renderItem(this, model)
}

/**
 * Renders an [ItemStack] at an [EntityCastable]
 */
fun ItemStack.drawItem(
    entity: Entity,
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f
) {
    GlStateManager.pushMatrix()

    GlStateManager.translate(x, y, z)

    this.render(entity.world)

    GlStateManager.popMatrix()
}
