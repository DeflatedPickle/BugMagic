/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object CapabilityEventHandler {
    @SubscribeEvent
    @JvmStatic
    fun onAttachCapabilitiesEventEntity(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is EntityPlayer) {
            event.addCapability(BugEssenceCapability.NAME, BugEssenceCapability.Provider())
            event.addCapability(SpellLearnerCapability.NAME, SpellLearnerCapability.Provider())
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onAttachCapabilitiesEventItemStack(event: AttachCapabilitiesEvent<ItemStack>) {
        if (event.`object`.item is Wand) {
			event.addCapability(SpellCasterCapability.NAME, SpellCasterCapability.Provider())
			event.addCapability(BugEssenceCapability.NAME, BugEssenceCapability.Provider())
        }
    }
}
