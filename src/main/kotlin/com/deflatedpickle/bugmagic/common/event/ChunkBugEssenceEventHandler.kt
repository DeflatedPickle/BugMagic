/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeBeach
import net.minecraft.world.biome.BiomeDesert
import net.minecraft.world.biome.BiomeForest
import net.minecraft.world.biome.BiomeHills
import net.minecraft.world.biome.BiomeJungle
import net.minecraft.world.biome.BiomeMesa
import net.minecraft.world.biome.BiomeMushroomIsland
import net.minecraft.world.biome.BiomeOcean
import net.minecraft.world.biome.BiomePlains
import net.minecraft.world.biome.BiomeRiver
import net.minecraft.world.biome.BiomeSavanna
import net.minecraft.world.biome.BiomeSnow
import net.minecraft.world.biome.BiomeStoneBeach
import net.minecraft.world.biome.BiomeSwamp
import net.minecraft.world.biome.BiomeTaiga
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
