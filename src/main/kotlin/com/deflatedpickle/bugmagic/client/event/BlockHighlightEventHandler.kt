package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.IBoundingBox
import com.deflatedpickle.bugmagic.api.client.util.OpenGL
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object BlockHighlightEventHandler {
    @SubscribeEvent
    fun onDrawBlockHighlight(event: DrawBlockHighlightEvent) {
        when (event.target.typeOfHit) {
            RayTraceResult.Type.BLOCK -> {
                with(event.player.world.getBlockState(event.target.blockPos).block) {
                    when (this) {
                        is IBoundingBox -> {
                            val player = event.player

                            for (i in this.boundingBoxList) {
                                OpenGL.drawSelectionBox(i, event.target.blockPos, player, event.partialTicks)
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}