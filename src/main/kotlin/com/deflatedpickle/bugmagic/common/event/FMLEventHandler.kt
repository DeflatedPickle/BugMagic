package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.capability.IBugEssence
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class FMLEventHandler {
    @SubscribeEvent
    fun onPlayerLoggedInEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        if (!event.player.world.isRemote) {
            if (event.player.hasCapability(BugEssence.Provider.CAPABILITY!!, null)) {
                with((event.player.getCapability(BugEssence.Provider.CAPABILITY!!, null) as IBugEssence)) {
                    this.max = 128
                    this.current = 64

                    BugMagic.CHANNEL.sendTo(MessageBugEssence(this.max, this.current), event.player as EntityPlayerMP)
                }
            }
        }
    }
}