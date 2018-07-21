package com.deflatedpickle.bugmagic.events

import com.deflatedpickle.bugmagic.items.ItemWand
import com.deflatedpickle.bugmagic.util.BugUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        val player = Minecraft.getMinecraft().player
        val playerData = player.entityData

        // Sets the initial Bug Power value
        // TODO: Move this to the PlayerEvent.PlayerLoggedInEvent event
        if (!playerData.getBoolean("bugmagic.initPlayer")) {
            playerData.setBoolean("bugmagic.initPlayer", true)
            BugUtil.setBugPower(player, 500)
        }

        if (player.heldItemMainhand.item is ItemWand) {
            Minecraft.getMinecraft().fontRenderer.drawString("%sBug Power: %d/%d".format(TextFormatting.WHITE,
                    BugUtil.getBugPower(player), playerData.getInteger("maxBugPower")),
                    5, ScaledResolution(Minecraft.getMinecraft()).scaledHeight - 15, ScaledResolution(Minecraft.getMinecraft()).scaledWidth - 5)
        }
    }
}