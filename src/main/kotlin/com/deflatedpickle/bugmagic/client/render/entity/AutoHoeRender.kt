/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoeEntity
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager
import org.lwjgl.util.ReadableColor

/**
 * The renderer for [AutoFertilizerEntity]
 *
 * @author DeflatedPickle
 */
class AutoHoeRender(renderManager: RenderManager) :
        RenderCastable<AutoHoeEntity>(renderManager, 0f) {
    override fun doRender(entity: AutoHoeEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        if (entity.owner?.heldItemMainhand?.item is Wand) {
            drawLine(
                    entity,
                    entity.dataManager.get(AutoHoeEntity.dataHomePosition),
                    ReadableColor.RED
            )
        }
        GlStateManager.popMatrix()
    }
}
