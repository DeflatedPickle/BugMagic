package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.init.ModRenders
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.event.FMLInitializationEvent

class ClientProxy : CommonProxy() {
    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        ModRenders
    }

    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}