package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack
import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.entity.mob.EntityWurm
import com.deflatedpickle.bugmagic.render.entity.RenderBugpack
import com.deflatedpickle.bugmagic.render.entity.RenderFirefly
import com.deflatedpickle.bugmagic.render.entity.RenderWurm
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.RenderingRegistry

object ModRenders {
    init {
        RenderingRegistry.registerEntityRenderingHandler(EntityFirefly::class.java, RenderFirefly(Minecraft.getMinecraft().renderManager))
        RenderingRegistry.registerEntityRenderingHandler(EntityBugpack::class.java, RenderBugpack(Minecraft.getMinecraft().renderManager))
        RenderingRegistry.registerEntityRenderingHandler(EntityWurm::class.java, RenderWurm(Minecraft.getMinecraft().renderManager))
    }
}