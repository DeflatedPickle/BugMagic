package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.*
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.event.world.ChunkWatchEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Suppress("unused", "SpellCheckingInspection")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ChunkBugEssenceEventHandler {
	@SubscribeEvent
	fun onChunkWatchEvent(event: ChunkWatchEvent) {
		val chunk = event.chunkInstance ?: return
		val bugEssence = BugEssenceCapability.isCapable(chunk)

		if (bugEssence != null) {
			// This chunk has already been given values, ditch it
			if (bugEssence.max > 0 || bugEssence.current > 0) return

			var max = 0

			for (biomeID in chunk.biomeArray.toSet()) {
				when (Biome.REGISTRY.getObjectById(biomeID.toInt())) {
					is BiomeMushroomIsland -> {
						max += 3000
					}
					is BiomeSwamp -> {
						max += 1600
					}
					is BiomePlains -> {
						max += 1000
					}
					is BiomeForest -> {
						max += 800
					}
					is BiomeJungle -> {
						max += 600
					}
					is BiomeSavanna, is BiomeTaiga -> {
						max += 400
					}
					is BiomeDesert, is BiomeMesa,
					is BiomeHills -> {
						max += 200
					}
					is BiomeRiver, is BiomeBeach,
					is BiomeOcean, is BiomeSnow,
					is BiomeStoneBeach -> {
						max += 100
					}
				}
			}

			bugEssence.max = max

			for (player in chunk.world.playerEntities) {
				BugMagic.CHANNEL.sendTo(
					MessageChunkBugEssence(
						player.position,
						bugEssence.max,
						bugEssence.current
					), player as EntityPlayerMP
				)
			}
		}
	}
}
