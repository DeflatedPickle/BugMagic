package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.init.ModRenders
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import java.awt.Color

class ClientProxy : CommonProxy() {
    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        ModRenders

        Minecraft.getMinecraft().itemColors.registerItemColorHandler(object : IItemColor {
            override fun colorMultiplier(stack: ItemStack?, tintIndex: Int): Int {
                return if (tintIndex == 0) {
                    Color.decode("#BBFF70").rgb
                } else {
                    -1
                }
            }
        }, ModItems.bugJuice)
    }

    override fun getPlayer(): EntityPlayer? {
        return Minecraft.getMinecraft().player
    }
}