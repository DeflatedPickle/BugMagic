package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object PlayerEventHandler {
    @SubscribeEvent
    @JvmStatic
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
}