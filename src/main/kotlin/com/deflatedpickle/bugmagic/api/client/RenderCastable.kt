/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client

import com.deflatedpickle.bugmagic.api.client.util.OpenGL
import com.deflatedpickle.bugmagic.api.client.util.extension.render
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11
import org.lwjgl.util.ReadableColor

abstract class RenderCastable<T : EntityCastable>(renderManager: RenderManager, shadowSize: Float) : RenderLiving<T>(renderManager, object : ModelBase() {}, shadowSize) {
    private val tessellator = Tessellator.getInstance()

    /**
     * Draws a line from an [EntityCastable] to a target [BlockPos], using a [ReadableColor]
     */
    fun drawInventoryLine(
        entity: T,
        target: BlockPos,
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

        tessellator.buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        tessellator.buffer.pos(0.0, 0.0, 0.0).endVertex()
        tessellator.buffer.pos(
                target.x - entity.posX + 0.5,
                target.y - entity.posY + 0.5,
                target.z - entity.posZ + 0.5
        ).endVertex()

        tessellator.draw()

        GlStateManager.enableTexture2D()

        GL11.glPopAttrib()
    }

    /**
     * Renders an [ItemStack] at an [EntityCastable]
     */
    fun drawItem(
        entity: T,
        itemStack: ItemStack
    ) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(0f, 0.2f, 0f)

        itemStack.render(entity.world)

        GlStateManager.popMatrix()
    }

    /**
     * Renders a this entities [AxisAlignedBB]
     */
    fun drawWorkArea(
        aabb: AxisAlignedBB,
        blockPos: BlockPos,
        player: EntityPlayer,
        partialTicks: Float
    ) {
        GlStateManager.pushMatrix()

        OpenGL.drawSelectionBox(aabb, blockPos, player, partialTicks)

        GlStateManager.popMatrix()
    }
}
