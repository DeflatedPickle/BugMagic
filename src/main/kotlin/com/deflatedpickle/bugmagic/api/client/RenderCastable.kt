/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client

import com.deflatedpickle.bugmagic.api.HasModel
import com.deflatedpickle.bugmagic.api.client.util.OpenGL
import com.deflatedpickle.bugmagic.api.client.util.extension.render
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11
import org.lwjgl.util.ReadableColor

abstract class RenderCastable<T : EntityCastable>(
    renderManager: RenderManager,
    shadowSize: Float
) : RenderLiving<T>(
        renderManager,
        object : ModelBase() {},
        shadowSize), HasModel {
    private val tessellator = Tessellator.getInstance()

    override fun getEntityTexture(entity: T): ResourceLocation? {
        return null
    }

    /**
     * Checks [hasModel] and if false, draws the AABB using [Render.renderOffsetAABB]
     */
    override fun doRender(entity: T, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        if (!this.hasModel()) {
            GlStateManager.pushMatrix()
            Render.renderOffsetAABB(entity.entityBoundingBox, x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ)
            GlStateManager.popMatrix()
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    /**
     * Draws a line from an [T] to a target [BlockPos], using a [ReadableColor]
     */
    fun drawLine(
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
        itemStack: ItemStack,
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f
    ) {
        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)

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
