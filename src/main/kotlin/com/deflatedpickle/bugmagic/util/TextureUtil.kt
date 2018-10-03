package com.deflatedpickle.bugmagic.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import javax.imageio.ImageIO

object TextureUtil {
    fun bufferedImageFromResourceLocation(resourceLocation: ResourceLocation): BufferedImage {
        val inputStream = Minecraft.getMinecraft().resourceManager.getResource(resourceLocation).inputStream

        return ImageIO.read(inputStream)
    }

    // Credit: https://stackoverflow.com/a/14513703
    fun applyGrayscale(resourceLocation: ResourceLocation): BufferedImage {
        val image = bufferedImageFromResourceLocation(resourceLocation)

        val colourConvertOP = ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null)
        colourConvertOP.filter(image, image)

        return image
    }

    // Credit:
    // http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Change_color_of_png_format_image.htm
    // https://stackoverflow.com/a/38731130
    fun applyColour(resourceLocation: ResourceLocation, r: Int, g: Int, b: Int): BufferedImage {
        val image = applyGrayscale(resourceLocation)
        val raster = image.raster

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val pixel = raster.getPixel(x, y, null as IntArray?)

                pixel[0] = r * pixel[0] / 255
                pixel[1] = g * pixel[1] / 255
                pixel[2] = b * pixel[2] / 255

                raster.setPixel(x, y, pixel)
            }
        }

        return image
    }

    fun recolourTexture(resourceLocation: ResourceLocation, name: String, r: Int, g: Int, b: Int): ResourceLocation {
        val image = applyColour(resourceLocation, r, g, b)

        return registerTexture(image, name)
    }

    fun recolourTexture(resourceLocation: ResourceLocation, name: String, hex: String): ResourceLocation {
        val colour = Color.decode(hex)

        return recolourTexture(resourceLocation, name, colour.red, colour.green, colour.blue)
    }

    fun registerTexture(texture: BufferedImage, name: String): ResourceLocation {
        val dynamicTexture = DynamicTexture(texture)

        val resourceLocation = ResourceLocation(name)
        Minecraft.getMinecraft().textureManager.loadTexture(resourceLocation, dynamicTexture)

        return resourceLocation
    }
}