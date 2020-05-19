/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.AbstractModel
import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorEntity
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager

/**
 * The renderer for [EssenceCollectorEntity]
 *
 * @author DeflatedPickle
 */
class EssenceCollectorRender(renderManager: RenderManager) :
        RenderCastable<EssenceCollectorEntity>(renderManager, 0.5f) {
    companion object : AbstractModel("essence_collector")

    override fun hasModel(): Boolean = true

    override fun doRender(entity: EssenceCollectorEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.rotate(entityYaw, 0f, 1f, 0f)

        val time = (entity.ticksExisted and 0xFFFFFF).toDouble() + partialTicks
        models["Idle"]!!.render(time)

        GlStateManager.popMatrix()
    }
}
