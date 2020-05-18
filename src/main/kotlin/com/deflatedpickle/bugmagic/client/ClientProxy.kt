/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client

import com.deflatedpickle.bugmagic.client.event.GameOverlayEventHandler
import com.deflatedpickle.bugmagic.common.CommonProxy
import com.deflatedpickle.bugmagic.common.init.RenderInit
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)

        RenderInit
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)

        MinecraftForge.EVENT_BUS.register(GameOverlayEventHandler)
    }

    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}
