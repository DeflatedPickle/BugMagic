package com.deflatedpickle.bugmagic.client

import com.deflatedpickle.bugmagic.client.event.ForgeEventHandler
import com.deflatedpickle.bugmagic.common.Proxy
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent

class Proxy : Proxy() {
    override fun init(event: FMLInitializationEvent) {
        super.init(event)

        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
    }

    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}