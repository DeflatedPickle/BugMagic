/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.client.networking.handler.HandlerSelectedSpell
import com.deflatedpickle.bugmagic.client.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.init.EntityInit
import com.deflatedpickle.bugmagic.common.init.FluidInit
import com.deflatedpickle.bugmagic.common.init.SmeltingInit
import com.deflatedpickle.bugmagic.common.init.TileEntityInit
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerChunkBugEssence
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerEntityTasks
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerPlayerBugEssence
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSpellCaster
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSpellChange
import com.deflatedpickle.bugmagic.common.networking.message.Message
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageEntityTasks
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellChange
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
        // Server->Client Packets
        BugMagic.CHANNEL.registerMessage(
            HandlerPlayerBugEssence::class.java, MessagePlayerBugEssence::class.java,
            Message.BUG_ESSENCE.ordinal, Side.CLIENT
        )
        BugMagic.CHANNEL.registerMessage(
            HandlerSpellCaster::class.java, MessageSpellCaster::class.java,
            Message.SPELL_CASTER.ordinal, Side.CLIENT
        )
        BugMagic.CHANNEL.registerMessage(
            HandlerEntityTasks::class.java, MessageEntityTasks::class.java,
            Message.ENTITY_TASKS.ordinal, Side.CLIENT
        )
        BugMagic.CHANNEL.registerMessage(
            HandlerSpellChange::class.java, MessageSpellChange::class.java,
            Message.SPELL_CHANGE.ordinal, Side.CLIENT
        )
        BugMagic.CHANNEL.registerMessage(
            HandlerChunkBugEssence::class.java, MessageChunkBugEssence::class.java,
            Message.CHUNK_ESSENCE.ordinal, Side.CLIENT
        )

        // Client->Server Packets
        BugMagic.CHANNEL.registerMessage(
            HandlerSelectedSpell::class.java, MessageSelectedSpell::class.java,
            Message.SELECTED_SPELL.ordinal, Side.SERVER
        )
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
