/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.api.client.util.extension.drawLine
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoeEntity
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.math.BlockPos
import org.lwjgl.util.ReadableColor

/**
 * The renderer for [AutoFertilizerEntity]
 *
 * @author DeflatedPickle
 */
class AutoHoeRender(renderManager: RenderManager) :
        RenderCastable<AutoHoeEntity>(renderManager, 0f)
