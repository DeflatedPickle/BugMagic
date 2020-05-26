package com.deflatedpickle.bugmagic.common.command

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.init.SpellInit
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellChange
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString

class CommandClearSpell : CommandBase() {
	val names = listOf("clearspell", "bmcs")

	override fun getName(): String = this.names[0]
	override fun getUsage(p0: ICommandSender): String =
		"${this.names[0]} (\"all\" | <spell name>)"

	override fun getAliases(): List<String> = this.names

	override fun getTabCompletions(p0: MinecraftServer, p1: ICommandSender, p2: Array<String>, p3: BlockPos?): List<String> {
		return when (p2.size) {
			1 -> mutableListOf("all").apply {
				addAll(
					(SpellInit.registry.valuesCollection as Set<Spell>).map { it.registryName.toString() }
				)
			}
			else -> listOf()
		}
	}

	override fun getRequiredPermissionLevel(): Int = 3

	override fun execute(p0: MinecraftServer, p1: ICommandSender, p2: Array<String>) {
		if (!p1.entityWorld.isRemote) {
			if (p2.isEmpty()) {
				p1.sendMessage(TextComponentString("Invalid argument length"))
			} else {
				if (p1 is EntityLivingBase) {
					val spellLearner = SpellLearnerCapability.isCapable(p1)

					if (spellLearner != null) {
						when (p2[0]) {
							"all" -> {
								spellLearner.spellList.clear()
								BugMagic.CHANNEL.sendToAll(
									MessageSpellChange(p1.entityId, spellLearner.spellList)
								)
							}
							else -> {
								for (i in SpellInit.registry.valuesCollection) {
									if (p2[0] == i.registryName.toString()) {
										spellLearner.spellList.remove(i)
									}
								}
								BugMagic.CHANNEL.sendToAll(
									MessageSpellChange(p1.entityId, spellLearner.spellList)
								)
							}
						}
					}
				}
			}
		}
	}
}
