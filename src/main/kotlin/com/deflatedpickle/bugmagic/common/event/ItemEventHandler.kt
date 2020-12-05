/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ItemEventHandler {
    @SubscribeEvent
    @JvmStatic
    fun onItemPickupEvent(event: PlayerEvent.ItemPickupEvent) {
        val spellLearner = SpellLearnerCapability.isCapable(event.player)
        val spellCaster = SpellCasterCapability.isCapable(event.player.heldItemMainhand)

        if (spellLearner != null && spellCaster != null) {
            spellCaster.owner = event.player.uniqueID
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onItemCraftedEvent(event: PlayerEvent.ItemCraftedEvent) {
        val spellLearner = SpellLearnerCapability.isCapable(event.player)
        val spellCaster = SpellCasterCapability.isCapable(event.player.heldItemMainhand)

        if (spellLearner != null && spellCaster != null) {
            spellCaster.owner = event.player.uniqueID
        }
    }
}
