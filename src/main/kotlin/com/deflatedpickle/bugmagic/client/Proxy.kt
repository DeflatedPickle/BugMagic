/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client

import com.deflatedpickle.bugmagic.client.event.ForgeEventHandler
import com.deflatedpickle.bugmagic.client.event.pre.Handler
import com.deflatedpickle.bugmagic.common.Proxy
import com.deflatedpickle.bugmagic.common.init.Render
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class Proxy : Proxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)

        Render

        MinecraftForge.EVENT_BUS.register(Handler())
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)

        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
    }

    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}
