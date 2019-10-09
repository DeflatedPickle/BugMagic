package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onAttachCapabilitiesEvent(event: AttachCapabilitiesEvent<Entity>) {
        // TODO: Also apply the capability to horses, donkeys and mules - weighing them down with armour and inventory
        if (event.`object` is EntityPlayer) {
            event.addCapability(BugEssence.NAME, BugEssence.Provider())
        }
    }
}