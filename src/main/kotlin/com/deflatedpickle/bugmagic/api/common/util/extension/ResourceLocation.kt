/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import com.deflatedpickle.bugmagic.api.common.util.OpenGL
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation

fun ResourceLocation.renderCube(width: Double, height: Double, depth: Double) {
    val sprite = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(this.toString())
    Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

    OpenGL.cube(width, height, depth, sprite.minU, sprite.minV, sprite.maxU, sprite.maxV)
}
