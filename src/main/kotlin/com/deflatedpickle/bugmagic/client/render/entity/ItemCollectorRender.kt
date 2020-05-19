/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.AbstractModel
import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.math.AxisAlignedBB
import org.lwjgl.util.ReadableColor

/**
 * The renderer for [ItemCollectorEntity]
 *
 * @author DeflatedPickle
 */
class ItemCollectorRender(renderManager: RenderManager) :
        RenderCastable<ItemCollectorEntity>(renderManager, 0f) {
    companion object : AbstractModel("item_collector") {
        val axisAlignedBB = AxisAlignedBB(
                -6.0, 0.1, -6.0,
                7.0, 1.0, 7.0
        )
    }

    override fun hasModel(): Boolean = true

    override fun doRender(entity: ItemCollectorEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()

        if (entity.owner?.heldItemMainhand?.item is Wand) {
            val blockPos = entity.dataManager.get(ItemCollectorEntity.dataInventoryPosition)
            this.drawWorkArea(
                    axisAlignedBB,
                    blockPos,
                    Minecraft.getMinecraft().player,
                    partialTicks
            )
        }

        GlStateManager.translate(x, y, z)

        if (entity.owner?.heldItemMainhand?.item is Wand) {
            drawLine(
                    entity,
                    entity.dataManager.get(ItemCollectorEntity.dataInventoryPosition),
                    ReadableColor.RED
            )
        }

        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        drawItem(entity, entity.dataManager.get(ItemCollectorEntity.dataItemStack), y = 0.2f)

        val time = (entity.ticksExisted and 0xFFFFFF).toDouble() + partialTicks
        models["Idle"]!!.render(time)

        GlStateManager.popMatrix()
    }
}
