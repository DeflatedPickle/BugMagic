/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object CastingCameraEventHandler {
    private var perspective = 0

    @SubscribeEvent
    fun onMouse(event: MouseEvent) {
        val options = Minecraft.getMinecraft().renderManager.options

        // I've crashed on the first time I joined a world because of this,
        // So if we schedule it... hopefully it won't cause a crash
        Minecraft.getMinecraft().addScheduledTask {
            val player = BugMagic.proxy!!.getPlayer()

            player?.let { entityPlayer ->
                val stack = entityPlayer.getHeldItem(entityPlayer.activeHand)
                val spellCaster = SpellCasterCapability.isCapable(stack)

                spellCaster?.let {
                    when (event.button) {
                        1 -> {
                            when (event.isButtonstate) {
                                true -> this.perspective = options.thirdPersonView
                                // This is checked here because if it was checked earlier
                                // It wouldn't set the perspective to the current view
                                // So it'd always default to 0
                                false -> if (it.isCasting)
                                    options.thirdPersonView = this.perspective
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun cameraSetupEvent(event: EntityViewRenderEvent.CameraSetup) {
        val entity = event.entity

        if (entity is EntityPlayerSP) {
            val options = Minecraft.getMinecraft().renderManager.options

            // It warns you it's never null but don't trust it
            // It's crashed me before, and it'll crash again!
            @Suppress("UNNECESSARY_SAFE_CALL")
            options?.let { gameSettings ->
                val view = gameSettings.thirdPersonView

                val hand = entity.activeHand

                hand?.let { enumHand ->
                    val stack = entity.getHeldItem(enumHand)
                    val spellCaster = SpellCasterCapability.isCapable(stack)

                    if (spellCaster != null) {
                        if (view == 0 || view == 2) {
                            if (spellCaster.isCasting) {
                                gameSettings.thirdPersonView = 1
                            }
                        }
                    }
                }
            }
        }
    }
}
