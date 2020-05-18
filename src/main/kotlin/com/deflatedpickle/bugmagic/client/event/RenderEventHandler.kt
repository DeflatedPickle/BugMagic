/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.IBoundingBox
import com.deflatedpickle.bugmagic.api.client.util.OpenGL
import com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.item.Wand
import com.github.upcraftlp.glasspane.api.event.client.RegisterRenderLayerEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object RenderEventHandler {
    val textHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 1.4f
    var textHeightPadding = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 0.6f

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent) {
        with(Minecraft.getMinecraft().player) {
            var y = 2f

            if (this.hasCapability(BugEssence.Provider.CAPABILITY, null)) {
                this.getCapability(BugEssence.Provider.CAPABILITY, null).also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}Bug Essence: ${it!!.current}/${it.max}",
                            2f, y, 0, true
                    )
                }
            }

            y += textHeight * 1.4f

            if (this.hasCapability(SpellLearner.Provider.CAPABILITY, null)) {
                this.getCapability(SpellLearner.Provider.CAPABILITY, null).also {
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
    fun onMouse(event: MouseEvent) {
        val player = BugMagic.proxy!!.getPlayer()
        if (player!!.heldItemMainhand.item is Wand && player.isSneaking &&
                (event.dwheel == -120 || event.dwheel == 120)) {
            // FIXME: If you scroll too quickly, it can still sometimes move the selected slot
            // Maybe don't actually fix this, because it's fine if you scroll slowly
            // Anyone who reports this was rapidly scrolling through their spells like an idiot
            (player.heldItemMainhand.item as Wand).onMouseEvent()

            event.isCanceled = true
            // You thought it was a MouseEvent. But it was I, Dio!
        }
    }

    @SubscribeEvent
    fun onRegisterRenderLayer(event: RegisterRenderLayerEvent) {
        event.addRenderLayer(LayerCastingShape())
    }

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
        }
    }
}