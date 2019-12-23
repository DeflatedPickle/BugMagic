/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager

fun FontRenderer.drawNameTag(string: String, x: Int, y: Int, colour: Int = 0xFFFFFF) {
    GlStateManager.pushMatrix()

    GlStateManager.translate(0.0, 0.4, 0.0)
    GlStateManager.rotate(180f - Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
    GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
    GlStateManager.scale(0.0075F, -0.0075F, 0.0075F)

    this.drawString(string, x - this.getStringWidth(string) / 2, y - this.FONT_HEIGHT / 2, colour)

    GlStateManager.popMatrix()
}
