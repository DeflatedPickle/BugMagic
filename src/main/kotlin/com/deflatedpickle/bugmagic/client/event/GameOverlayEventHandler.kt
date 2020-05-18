/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.github.upcraftlp.glasspane.api.event.client.RegisterRenderLayerEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GameOverlayEventHandler {
    // This might break with GUI scaling? Maybe use a percentage of the font size
    val textHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 1.4f
    // The 0.6f just happens to be a nice gap size
    // This might also break with GUI scaling
    var textHeightPadding = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 0.6f

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent) {
        with(Minecraft.getMinecraft().player) {
            var y = 2f

            // Renders the bug essence string
            // TODO: The bug essence text looks boring, make a bar asset for it
            if (this.hasCapability(BugEssenceCapability.Provider.CAPABILITY, null)) {
                this.getCapability(BugEssenceCapability.Provider.CAPABILITY, null).also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}Bug Essence: ${it!!.current}/${it.max}",
                            2f, y, 0, true
                    )
                }
            }

            y += textHeight * 1.4f

            // Renders the players spell library
            // This is only for debug
            // TODO: Add a spell book to replace debug text
            if (this.hasCapability(SpellLearnerCapability.Provider.CAPABILITY, null)) {
                this.getCapability(SpellLearnerCapability.Provider.CAPABILITY, null).also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}${TextFormatting.UNDERLINE}Spell Library:",
                            2f, y, 0, true
                    )

                    y += textHeight

                    if (it!!.spellList.size > 0) {
                        for ((index, spell) in it.spellList.withIndex()) {
                            Minecraft.getMinecraft().fontRenderer.drawString(
                                    "${TextFormatting.GOLD}[${spell.type.name.toLowerCase().capitalize()}] ${TextFormatting.WHITE}${spell.name}",
                                    // ${TextFormatting.ITALIC}(Costs: ${spell.manaLoss}, Summons: ${spell.castCount})
                                    2f, y + index * textHeightPadding, 0, true
                            )
                        }
                    } else {
                        Minecraft.getMinecraft().fontRenderer.drawString(
                                "${TextFormatting.WHITE}None",
                                2f, y, 0, true
                        )
                    }
                }
            }

            Minecraft.getMinecraft().textureManager.bindTexture(Gui.ICONS)
        }
    }

    @SubscribeEvent
    fun onRegisterRenderLayer(event: RegisterRenderLayerEvent) {
        event.addRenderLayer(LayerCastingShape())
    }
}
