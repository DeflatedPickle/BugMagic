/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellChange
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
			val bugEssence = BugEssenceCapability.isCapable(event.player)

			if (bugEssence != null) {
				bugEssence.max = 128
				bugEssence.current = 64

				BugMagic.CHANNEL.sendTo(
					MessageBugEssence(
						event.player.entityId,
						bugEssence.max,
						bugEssence.current),
					event.player as EntityPlayerMP
				)
			}

			SpellLearnerCapability.isCapable(event.player)?.let {
				BugMagic.CHANNEL.sendToAll(MessageSpellChange(event.player.entityId, it.spellList))
			}
		}
	}
}
