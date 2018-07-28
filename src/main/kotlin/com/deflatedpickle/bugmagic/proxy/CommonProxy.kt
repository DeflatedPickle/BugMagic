package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.events.ForgeEventHandler
import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModEntities
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.network.PacketBugPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side


open class CommonProxy {
    fun preInit(event: FMLPreInitializationEvent) {
        BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.CLIENT)

        ModCreativeTabs
        ModItems
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
        ModEntities
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}