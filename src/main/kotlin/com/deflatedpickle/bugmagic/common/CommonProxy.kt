/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.init.EntityInit
import com.deflatedpickle.bugmagic.common.init.FluidInit
import com.deflatedpickle.bugmagic.common.init.SmeltingInit
import com.deflatedpickle.bugmagic.common.init.TileEntityInit
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerBugEssence
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSelectedSpell
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSpellCaster
import com.deflatedpickle.bugmagic.common.networking.message.Message
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

/**
 * A proxy to register common objects
 *
 * @author DeflatedPickle
 */
open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        // Statically initializes objects
        FluidInit
        SmeltingInit
        TileEntityInit

        // Register capabilities
        BugEssenceCapability.register()
        SpellLearnerCapability.register()
        SpellCasterCapability.register()

        // Register packets
        BugMagic.CHANNEL.registerMessage(HandlerBugEssence::class.java, MessageBugEssence::class.java, Message.BUG_ESSENCE.ordinal, Side.CLIENT)
        BugMagic.CHANNEL.registerMessage(HandlerSelectedSpell::class.java, MessageSelectedSpell::class.java, Message.SELECTED_SPELL.ordinal, Side.SERVER)
        BugMagic.CHANNEL.registerMessage(HandlerSpellCaster::class.java, MessageSpellCaster::class.java, Message.SPELL_CASTER.ordinal, Side.CLIENT)
    }

    open fun init(event: FMLInitializationEvent) {
        // Statically initialize entities
        EntityInit
    }

    open fun postInit(event: FMLPostInitializationEvent) {
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}
