/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.init.Spell
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onAttachCapabilitiesEventEntity(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is EntityPlayer) {
            event.addCapability(BugEssence.NAME, BugEssence.Provider())
            event.addCapability(SpellLearner.NAME, SpellLearner.Provider())
        }
    }

    @SubscribeEvent
    fun onAttachCapabilitiesEventItemStack(event: AttachCapabilitiesEvent<ItemStack>) {
        if (event.`object`.item is Wand) {
            event.addCapability(SpellCaster.NAME, SpellCaster.Provider())
        }
    }

    @SubscribeEvent
    fun onRegisterEventNewRegistry(event: RegistryEvent.NewRegistry) {
        Spell.registry
    }
}
