/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util.extension

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11
import org.lwjgl.util.ReadableColor

/**
 * Draws a line from an [Vec3d] to a target [Vec3d], using a [ReadableColor]
 */
fun Tessellator.drawLine(
    start: Vec3d,
    target: Vec3d,
    colour: ReadableColor
) {
    GL11.glPushAttrib(GL11.GL_CURRENT_BIT)

    GlStateManager.disableTexture2D()

    GlStateManager.color(
        colour.red / 255f,
        colour.green / 255f,
        colour.blue / 255f,
        colour.alpha / 255f
    )

    this.buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
    this.buffer.pos(0.0, 0.0, 0.0).endVertex()
    this.buffer.pos(
        target.x - start.x,
        target.y - start.y,
        target.z - start.z
    ).endVertex()

    this.draw()

    GlStateManager.enableTexture2D()

    GL11.glPopAttrib()
}

/**
 * Draws a line from an [T] to a target [Vec3d], using a [ReadableColor]
 */
fun Tessellator.drawLine(
    entity: Entity,
    target: Vec3d,
    colour: ReadableColor
) {
    this.drawLine(Vec3d(entity.posX, entity.posY, entity.posZ), target, colour)
}

/**
 * Draws a line from an [T] to a target [BlockPos], using a [ReadableColor]
 */
fun Tessellator.drawLine(
    entity: Entity,
    target: BlockPos,
    colour: ReadableColor
) {
    this.drawLine(
        Vec3d(entity.posX, entity.posY, entity.posZ),
        Vec3d(target.x + 0.5, target.y.toDouble(), target.z.toDouble() + 0.5),
        colour
    )
}

/**
 * Draws a line from an [Vec3d] to a target [BlockPos], using a [ReadableColor]
 */
fun Tessellator.drawLine(
    start: Vec3d,
    target: BlockPos,
    colour: ReadableColor
) {
    this.drawLine(
        start,
        Vec3d(target.x + 0.5, target.y.toDouble(), target.z + 0.5),
        colour
    )
}
