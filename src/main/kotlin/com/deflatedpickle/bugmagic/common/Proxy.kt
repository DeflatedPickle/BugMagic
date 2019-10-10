package com.deflatedpickle.bugmagic.common

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.event.FMLEventHandler
import com.deflatedpickle.bugmagic.common.event.ForgeEventHandler
import com.deflatedpickle.bugmagic.common.init.Recipe
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.Message
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

open class Proxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        Recipe

        BugEssence.register()

        BugMagic.CHANNEL.registerMessage(HandlerBugEssence::class.java, MessageBugEssence::class.java, Message.BUG_ESSENCE.ordinal, Side.CLIENT)
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(FMLEventHandler())
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
    }

    open fun postInit(event: FMLPostInitializationEvent) {
    }
}