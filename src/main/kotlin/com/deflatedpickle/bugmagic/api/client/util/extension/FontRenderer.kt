/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util.extension

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper

/**
 * Draws a series of strings, split at newlines, that face the player
 */
fun FontRenderer.drawNameTag(
    string: String,
    x: Int,
    y: Int,
    colour: Int = 0xFFFFFF
) {
    RenderHelper.disableStandardItemLighting()

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

    RenderHelper.enableStandardItemLighting()
}

/**
 * Draws a series of strings that face the player
 */
fun FontRenderer.drawNameTag(
    x: Int,
    y: Int,
    colour: Int = 0xFFFFFF,
    vararg string: String
) = this.drawNameTag(string.joinToString("\n"), x, y, colour)

/**
 * Draws a flat string with culling disabled
 */
fun FontRenderer.drawStillNameTag(
    string: String,
    x: Int,
    y: Int,
    colour: Int = 0xFFFFFF
) {
    RenderHelper.disableStandardItemLighting()

    GlStateManager.disableCull()
    GlStateManager.pushMatrix()

    GlStateManager.translate(0.0, 0.4, 0.0)
    GlStateManager.scale(0.0075F, -0.0075F, 0.0075F)

    val lines = string.split("\n")

    for ((i, str) in lines.reversed().withIndex()) {
        this.drawString(str, x - this.getStringWidth(str) / 2, y - i * (this.FONT_HEIGHT / 2) * lines.count(), colour)
    }

    GlStateManager.popMatrix()
    GlStateManager.enableCull()

    RenderHelper.enableStandardItemLighting()
}

/**
 * Draws a flat string with culling disabled
 */
fun FontRenderer.drawStillNameTag(
    x: Int,
    y: Int,
    colour: Int = 0xFFFFFF,
    vararg string: String
) = this.drawStillNameTag(string.joinToString("\n"), x, y, colour)
