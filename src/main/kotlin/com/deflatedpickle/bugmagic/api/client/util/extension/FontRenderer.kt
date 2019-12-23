/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util.extension

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager

fun FontRenderer.drawNameTag(string: String, x: Int, y: Int, colour: Int = 0xFFFFFF) {
    GlStateManager.pushMatrix()

    GlStateManager.translate(0.0, 0.4, 0.0)
    GlStateManager.rotate(180f - Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
    GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
    GlStateManager.scale(0.0075F, -0.0075F, 0.0075F)

    val lines = string.split("\n")

    for ((i, str) in lines.reversed().withIndex()) {
        this.drawString(str, x - this.getStringWidth(str) / 2, y - i * (this.FONT_HEIGHT / 2) * lines.count(), colour)
    }

    GlStateManager.popMatrix()
}
