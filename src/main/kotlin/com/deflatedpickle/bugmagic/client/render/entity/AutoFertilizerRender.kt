/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.entity

import com.deflatedpickle.bugmagic.api.client.RenderCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import net.minecraft.client.renderer.entity.RenderManager

/**
 * The renderer for [AutoFertilizerEntity]
 *
 * @author DeflatedPickle
 */
class AutoFertilizerRender(renderManager: RenderManager) :
        RenderCastable<AutoFertilizerEntity>(renderManager, 0f)
