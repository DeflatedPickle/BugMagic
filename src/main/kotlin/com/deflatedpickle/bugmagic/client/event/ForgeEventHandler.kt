package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        with(Minecraft.getMinecraft().player) {
            if (this.hasCapability(BugEssence.Provider.CAPABILITY!!, null)) {
                this.getCapability(BugEssence.Provider.CAPABILITY!!, null)!!.also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}Bug Essence: ${it.current}/${it.max}",
                            2f, 2f, 0, true
                    )
                }
            }
        }
    }
}