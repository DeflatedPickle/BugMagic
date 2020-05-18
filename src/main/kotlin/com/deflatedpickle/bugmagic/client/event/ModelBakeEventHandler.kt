/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.client.render.entity.EssenceCollectorRender
import com.deflatedpickle.bugmagic.client.render.entity.ItemCollectorRender
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ModelBakeEventHandler {
    @SubscribeEvent
    @JvmStatic
    fun onModelBakeEvent(event: ModelBakeEvent) {
        ItemCollectorRender.reloadModels()
        EssenceCollectorRender.reloadModels()
    }
}
