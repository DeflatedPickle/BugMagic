/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class FMLEventHandler {
    @SubscribeEvent
    fun onPlayerLoggedInEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        if (!event.player.world.isRemote) {
            val bugEssence = BugEssence.isCapable(event.player)

            if (bugEssence != null) {
                bugEssence.max = 128
                bugEssence.current = 64

                BugMagic.CHANNEL.sendTo(MessageBugEssence(event.player.entityId, bugEssence.max, bugEssence.current), event.player as EntityPlayerMP)
            }
        }
    }

    @SubscribeEvent
    fun onItemPickupEvent(event: PlayerEvent.ItemPickupEvent) {
        val spellLearner = SpellLearner.isCapable(event.player)
        val spellCaster = SpellCaster.isCapable(event.player.heldItemMainhand)

        if (spellLearner != null && spellCaster != null) {
            spellCaster.owner = event.player.uniqueID
        }
    }

    @SubscribeEvent
    fun onItemCraftedEvent(event: PlayerEvent.ItemCraftedEvent) {
        val spellLearner = SpellLearner.isCapable(event.player)
        val spellCaster = SpellCaster.isCapable(event.player.heldItemMainhand)

        if (spellLearner != null && spellCaster != null) {
            spellCaster.owner = event.player.uniqueID
        }
    }
}
