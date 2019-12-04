package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollector
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import com.deflatedpickle.bugmagic.client.render.entity.ItemCollector as ItemCollectorRender
import com.deflatedpickle.bugmagic.client.render.entity.EssenceCollector as EssenceCollectorRender
import net.minecraftforge.fml.client.registry.RenderingRegistry

object Renders {
    init {
        RenderingRegistry.registerEntityRenderingHandler(ItemCollector::class.java, ::ItemCollectorRender)
        ItemCollectorRender.registerModels()

        RenderingRegistry.registerEntityRenderingHandler(EssenceCollector::class.java, ::EssenceCollectorRender)
        EssenceCollectorRender.registerModels()
    }
}