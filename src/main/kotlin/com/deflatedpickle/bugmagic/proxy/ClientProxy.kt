package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.init.ModRenders
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import java.awt.Color

class ClientProxy : CommonProxy() {
    override fun preInit() {
        super.preInit()
        ModRenders
    }

    override fun init() {
        super.init()

        Minecraft.getMinecraft().itemColors.registerItemColorHandler(IItemColor { _, tintIndex ->
            if (tintIndex == 0) {
                Color.decode("#BBFF70").rgb
            } else {
                -1
            }
        }, ModItems.BUG_JUICE)
    }

    override fun getPlayer(): EntityPlayer {
        return Minecraft.getMinecraft().player
    }
}