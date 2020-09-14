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

@Suppress("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object ChunkBugEssenceEventHandler {
	@SubscribeEvent
	fun onChunkWatchEvent(event: ChunkWatchEvent) {
		val chunk = event.chunkInstance ?: return

		val bugEssence = BugEssenceCapability.isCapable(chunk)

		if (bugEssence != null) {
			@Suppress("SpellCheckingInspection")
			var max = 0
			var current = 0

			for (biomeID in chunk.biomeArray.toSet()) {
				val biome = Biome.REGISTRY.getObjectById(biomeID.toInt())

				when (biome) {
					is BiomeMushroomIsland -> {
						max += 3000
						current += 2000
					}
					is BiomeSwamp -> {
						max += 1600
						current += 1000
					}
					is BiomePlains -> {
						max += 1000
						current += 200
					}
					is BiomeForest -> {
						max += 800
						current += 140
					}
					is BiomeJungle -> {
						max += 600
						current += 140
					}
					is BiomeSavanna, is BiomeTaiga -> {
						max += 400
						current += 60
					}
					is BiomeDesert, is BiomeMesa,
					is BiomeHills -> {
						max += 200
						current += 24
					}
					is BiomeRiver, is BiomeBeach,
					is BiomeOcean, is BiomeSnow,
					is BiomeStoneBeach -> {
						max += 100
						current += 8
					}
				}
			}

			bugEssence.max = max
			bugEssence.current = current

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
