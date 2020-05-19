/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvesterEntity
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager
import org.lwjgl.util.ReadableColor

/**
 * The renderer for [AutoHarvesterEntity]
 *
 * @author DeflatedPickle
 */
class AutoHarvesterRender(renderManager: RenderManager) :
        RenderCastable<AutoHarvesterEntity>(renderManager, 0f) {
    override fun doRender(entity: AutoHarvesterEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        if (entity.owner?.heldItemMainhand?.item is Wand) {
            drawLine(
                    entity,
                    entity.dataManager.get(AutoHarvesterEntity.dataHomePosition),
                    ReadableColor.RED
            )
        }
        GlStateManager.popMatrix()
    }
}
