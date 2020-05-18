/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.event.FMLEventHandler
import com.deflatedpickle.bugmagic.common.event.ForgeEventHandler
import com.deflatedpickle.bugmagic.common.init.Entity
import com.deflatedpickle.bugmagic.common.init.Fluid
import com.deflatedpickle.bugmagic.common.init.Smelting
import com.deflatedpickle.bugmagic.common.init.TileEntity
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerBugEssence
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSelectedSpell
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSpellCaster
import com.deflatedpickle.bugmagic.common.networking.message.Message
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

open class Proxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        Fluid
        Smelting
        TileEntity

        BugEssence.register()
        SpellLearner.register()
        SpellCaster.register()

        BugMagic.CHANNEL.registerMessage(HandlerBugEssence::class.java, MessageBugEssence::class.java, Message.BUG_ESSENCE.ordinal, Side.CLIENT)
        BugMagic.CHANNEL.registerMessage(HandlerSelectedSpell::class.java, MessageSelectedSpell::class.java, Message.SELECTED_SPELL.ordinal, Side.SERVER)
        BugMagic.CHANNEL.registerMessage(HandlerSpellCaster::class.java, MessageSpellCaster::class.java, Message.SPELL_CASTER.ordinal, Side.CLIENT)
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(FMLEventHandler())
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())

        Entity
    }

    open fun postInit(event: FMLPostInitializationEvent) {
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}
