/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.github.upcraftlp.glasspane.api.event.client.RegisterRenderLayerEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * An event handler for [RenderGameOverlayEvent] and [RegisterRenderLayerEvent]
 *
 * @author DeflatedPickle
 */
object GameOverlayEventHandler {
	// This might break with GUI scaling? Maybe use a percentage of the font size
	val textHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 1.4f

	// The 0.6f just happens to be a nice gap size
	// This might also break with GUI scaling
	var textHeightPadding = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 0.6f

	@SubscribeEvent
	fun onRenderGameOverlay(event: RenderGameOverlayEvent) {
		val player = Minecraft.getMinecraft().player
		val chunk = Minecraft.getMinecraft().world.getChunk(player.position)

		var y = 2f

		// Renders the bug essence string
		// TODO: The bug essence text looks boring, make a bar asset for it
		this.renderPlayerBugEssence(player, y)

		y += textHeight * 1.4f

		this.renderChunkBugEssence(chunk, y)

		y += textHeight * 1.4f

		// Renders the players spell library
		// This is only for debug
		// TODO: Add a spell book to replace debug text
		this.renderPlayerSpellList(player, y)

		Minecraft.getMinecraft().textureManager.bindTexture(Gui.ICONS)
	}

	@SubscribeEvent
	fun onRegisterRenderLayer(event: RegisterRenderLayerEvent) {
		event.addRenderLayer(LayerCastingShape())
	}

	private fun renderPlayerBugEssence(player: EntityPlayer, y: Float) {
		if (player.hasCapability(BugEssenceCapability.Provider.CAPABILITY, null)) {
			player.getCapability(BugEssenceCapability.Provider.CAPABILITY, null).also {
				Minecraft.getMinecraft().fontRenderer.drawString(
					"${TextFormatting.WHITE}Player Bug Essence: ${it!!.current}/${it.max}",
					2f, y, 0, true
				)
			}
		}
	}

	private fun renderPlayerSpellList(player: EntityPlayer, initialY: Float) {
		var y = initialY

		if (player.hasCapability(SpellLearnerCapability.Provider.CAPABILITY, null)) {
			player.getCapability(SpellLearnerCapability.Provider.CAPABILITY, null).also {
				Minecraft.getMinecraft().fontRenderer.drawString(
					"${TextFormatting.WHITE}${TextFormatting.UNDERLINE}Spell Library:",
					2f, y, 0, true
				)

				y += textHeight

				if (it!!.spellList.size > 0) {
					for ((index, spell) in it.spellList.withIndex()) {
						Minecraft.getMinecraft().fontRenderer.drawString(
							"${TextFormatting.GOLD}[${
								spell.type.name.toLowerCase().capitalize()
							}] ${TextFormatting.WHITE}${spell.name}",
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
	}

	private fun renderChunkBugEssence(chunk: Chunk, y: Float) {
		if (chunk.hasCapability(BugEssenceCapability.Provider.CAPABILITY, null)) {
			chunk.getCapability(BugEssenceCapability.Provider.CAPABILITY, null).also {
				Minecraft.getMinecraft().fontRenderer.drawString(
					"${TextFormatting.WHITE}Chunk Bug Essence: ${it!!.current}/${it.max}",
					2f, y, 0, true
				)
			}
		}
	}
}
