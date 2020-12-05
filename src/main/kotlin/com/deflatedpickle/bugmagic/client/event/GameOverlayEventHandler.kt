/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.api.common.util.function.*
import com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.github.upcraftlp.glasspane.api.event.client.RegisterRenderLayerEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumHand
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

		y += (textHeight * 1.4f)

		this.renderChunkBugEssence(chunk, y)

		y += textHeight * 1.4f

		// Renders the players spell library
		// This is only for debug
		// TODO: Add a spell book to replace debug text
		this.renderPlayerSpellList(player, y)

		// We have to do this or Minecraft's GUI breaks down
		Minecraft.getMinecraft().textureManager.bindTexture(Gui.ICONS)
	}

	@SubscribeEvent
	fun onRegisterRenderLayer(event: RegisterRenderLayerEvent) {
		event.addRenderLayer(LayerCastingShape())
	}

	private fun renderPlayerBugEssence(player: EntityPlayer, y: Float) {
		val bugEssence = BugEssenceCapability.isCapable(player)

		if (bugEssence != null) {
			Minecraft.getMinecraft().fontRenderer.drawString(
				green - "${bugEssence.current}/${bugEssence.max}" + (white - "BE"),
				2f, y, 0, true
			)
		}
	}

	private fun renderPlayerSpellList(player: EntityPlayer, initialY: Float) {
		var y = initialY

		val stack = player.getHeldItem(
			player.activeHand ?: EnumHand.MAIN_HAND
		)

		val spellLearner = SpellLearnerCapability.isCapable(player)
		val spellCaster = SpellCasterCapability.isCapable(stack)

		if (spellLearner != null) {
			Minecraft.getMinecraft().fontRenderer.drawString(
				"${TextFormatting.WHITE}${TextFormatting.UNDERLINE}Spell Library:",
				2f, y, 0, true
			)

			y += textHeight

			if (spellLearner.spellList.size > 0) {
				for ((index, spell) in spellLearner.spellList.withIndex()) {
					Minecraft.getMinecraft().fontRenderer.drawString(
						join(
							gold - "[${spell.type.name.toLowerCase().capitalize()}] " + (white - spell.name),
							green - spell.manaLoss + (white - "BE")
						),
						2f,
						y + (index * 2) * textHeightPadding, 0, true
					)

					Minecraft.getMinecraft().fontRenderer.drawString(
						join(
							white - "Cast: " + (blue - "${
								spellCaster
									?.castSpellMap
									?.getOrDefault(spell, 0) ?: 0
							}/${spell.maxCount}")
						),
						Minecraft.getMinecraft()
							.fontRenderer
							.getStringWidth("[${spell.type.name}] ").toFloat(),
						y + (index * 2 + 1) * textHeightPadding, 0, true
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

	private fun renderChunkBugEssence(chunk: Chunk, y: Float) {
		val bugEssence = BugEssenceCapability.isCapable(chunk)

		if (bugEssence != null) {
			Minecraft.getMinecraft().fontRenderer.drawString(
				"${
					TextFormatting.WHITE
				}Chunk Bug Essence: ${bugEssence.current}/${bugEssence.max}",
				2f, y, 0, true
			)
		}
	}
}
