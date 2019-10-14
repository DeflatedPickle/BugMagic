package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    val textHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 1.4f
    var textHeightPadding = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 0.6f

    @SubscribeEvent
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        with(Minecraft.getMinecraft().player) {
            var y = 2f

            if (this.hasCapability(BugEssence.Provider.CAPABILITY!!, null)) {
                this.getCapability(BugEssence.Provider.CAPABILITY!!, null)!!.also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}Bug Essence: ${it.current}/${it.max}",
                            2f, y, 0, true
                    )
                }
            }

            y += textHeight * 1.4f

            if (this.hasCapability(SpellLearner.Provider.CAPABILITY!!, null)) {
                this.getCapability(SpellLearner.Provider.CAPABILITY!!, null)!!.also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}${TextFormatting.UNDERLINE}Spell Library:",
                            2f, y, 0, true
                    )

                    y += textHeight

                    if (it.spellList.size > 0) {
                        for ((index, spell) in it.spellList.withIndex()) {
                            Minecraft.getMinecraft().fontRenderer.drawString(
                                    "${TextFormatting.WHITE}${spell.name}",
                                    2f, y + index * textHeightPadding, 0, true
                            )
                        }
                    }
                    else {
                        Minecraft.getMinecraft().fontRenderer.drawString(
                                "${TextFormatting.WHITE}None",
                                2f, y, 0, true
                        )
                    }
                }
            }
        }
    }
}