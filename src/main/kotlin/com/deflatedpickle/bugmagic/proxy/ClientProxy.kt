package com.deflatedpickle.bugmagic.proxy

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

class ClientProxy : CommonProxy() {
    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}