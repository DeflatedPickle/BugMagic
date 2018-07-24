package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.render.entity.RenderFirefly
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.RenderingRegistry

object ModRenders {
    init {
        RenderingRegistry.registerEntityRenderingHandler(EntityFirefly::class.java, RenderFirefly(Minecraft.getMinecraft().renderManager))
    }
}