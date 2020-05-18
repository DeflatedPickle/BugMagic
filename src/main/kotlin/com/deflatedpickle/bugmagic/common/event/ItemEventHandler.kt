/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ItemEventHandler {
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
