/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client

import com.deflatedpickle.bugmagic.api.HasModel
import com.deflatedpickle.bugmagic.api.client.util.OpenGLUtil
import com.deflatedpickle.bugmagic.api.client.util.extension.drawLine
import com.deflatedpickle.bugmagic.api.client.util.extension.drawNameTag
import com.deflatedpickle.bugmagic.api.common.util.AITaskString
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import org.lwjgl.util.ReadableColor

abstract class RenderCastable<T : EntityCastable>(
    renderManager: RenderManager,
    shadowSize: Float
) : RenderLiving<T>(
        renderManager,
        object : ModelBase() {},
        shadowSize), HasModel {
    protected val tessellator: Tessellator = Tessellator.getInstance()

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

		// Draw a line from the owner to this entity
        if (entity.owner?.heldItemMainhand?.item is Wand) {
            this.tessellator.drawLine(
                    entity.owner!!.positionVector,
                    entity.positionVector,
                    ReadableColor.BLUE
            )
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()

        GlStateManager.translate(x, y, z)

        entity.clientTasks?.let {
            Minecraft.getMinecraft().fontRenderer.drawNameTag(
                    string = "All Tasks:\n${prepareTaskString(it)}",
                    x = x.toInt() - 100, y = y.toInt()
            )
        }

        entity.clientExecutingTasks?.let {
            Minecraft.getMinecraft().fontRenderer.drawNameTag(
                    string = "Executing Tasks:\n${prepareTaskString(it)}",
                    x = x.toInt() + 100, y = y.toInt()
            )
        }

        GlStateManager.popMatrix()

		// Draw a line to the home position
		GlStateManager.pushMatrix()
		GlStateManager.translate(x, y, z)
		if (entity.owner?.heldItemMainhand?.item is Wand) {
			this.tessellator.drawLine(
				entity,
				entity.dataManager.get(EntityCastable.dataHomePosition),
				ReadableColor.RED
			)
		}
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

        OpenGLUtil.drawSelectionBox(aabb, blockPos, player, partialTicks)

        GlStateManager.popMatrix()
    }

	private fun prepareTaskString(tasks: Set<AITaskString>): String =
		tasks.sortedBy { it.priority }.map {
			"${it.priority}. ${it.action} (${it.using})"
		}.toString()
			.replace("[", "")
			.replace("]", "")
			.replace(",", "\n")
}
