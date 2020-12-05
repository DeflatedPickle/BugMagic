/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.api.client.util.extension.drawLine
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
        RenderCastable<AutoHarvesterEntity>(renderManager, 0f)
