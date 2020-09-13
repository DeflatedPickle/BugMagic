package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomePlains
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ChunkBugEssenceEventHandler {
	@SubscribeEvent
	fun onChunkLoadEvent(event: ChunkEvent.Load) {
		val chunk = event.chunk

		@Suppress("SpellCheckingInspection")
		val biomeList = arrayListOf<Biome>()

		/*for (x in 0..16) {
			for (z in 0..16) {
				@Suppress("SpellCheckingInspection")
				val tempBiome = chunk.getBiome(
					BlockPos(x, z, 0),
					event.`object`.world.biomeProvider
				).biomeClass.kotlin.objectInstance

				if (tempBiome!= null && !biomeList.contains(tempBiome)) {
					biomeList.add(tempBiome)
				}
			}
		}*/

		val bugEssence = BugEssenceCapability.isCapable(chunk)

		if (bugEssence != null) {
			@Suppress("SpellCheckingInspection")
			/*for (biome in biomeList) {
				when (biome) {
					else -> {
					}
				}
			}*/

			bugEssence.max = 500
			bugEssence.current = 200

			for (player in event.world.playerEntities) {
				BugMagic.CHANNEL.sendTo(
					MessageChunkBugEssence(
						BlockPos(chunk.x, chunk.z, 0),
						bugEssence.max,
						bugEssence.current
					), player as EntityPlayerMP
				)
			}
		}
	}
}
