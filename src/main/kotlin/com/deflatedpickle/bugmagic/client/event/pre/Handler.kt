/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event.pre

import com.deflatedpickle.bugmagic.client.render.entity.EssenceCollector
import com.deflatedpickle.bugmagic.client.render.entity.ItemCollector
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class Handler {
    @SubscribeEvent
    fun onModelBakeEvent(event: ModelBakeEvent) {
        ItemCollector.reloadModels()
        EssenceCollector.reloadModels()
    }
}
