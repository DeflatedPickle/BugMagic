package com.deflatedpickle.bugmagic.common.command

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import kotlin.math.max
import kotlin.math.min

class CommandSetEssence : CommandBase() {
	companion object {
		const val fillArgument = "max"
	}

	val names = listOf("setessence", "bmse")

	override fun getName(): String = this.names[0]
	override fun getUsage(p0: ICommandSender): String =
		"${this.names[0]} (\"player\" | \"item\" | \"chunk\") (\"$fillArgument\" | <int>)"

	override fun getAliases(): List<String> = this.names

	override fun getTabCompletions(
		p0: MinecraftServer,
		p1: ICommandSender,
		p2: Array<String>,
		p3: BlockPos?
	): List<String> {
		return when (p2.size) {
			1 -> mutableListOf(fillArgument)
			2 -> mutableListOf("player", "item", "chunk")
			else -> listOf()
		}
	}

	override fun getRequiredPermissionLevel(): Int = 3

	override fun execute(p0: MinecraftServer, p1: ICommandSender, p2: Array<String>) {
		if (!p1.entityWorld.isRemote) {
			if (p2.isEmpty()) {
				p1.sendMessage(TextComponentString("Invalid argument length"))
			} else {
				if (p1 is EntityLivingBase && (p2[1] == fillArgument || p2[0].toIntOrNull() != null)) {
					when (p2[0]) {
						"player" -> {
							val bugEssence = BugEssenceCapability.isCapable(p1)
							if (bugEssence != null) {
								when {
									p2[1] == fillArgument -> bugEssence.current = bugEssence.max
									p2[1].toIntOrNull() != null -> bugEssence.current = max(0, min(p2[1].toInt(), bugEssence.max))
									else -> p1.sendMessage(
											TextComponentString(
												"Second argument is not \"$fillArgument\" or an int"
											)
										)
								}

								BugMagic.CHANNEL.sendTo(
									MessagePlayerBugEssence(
										p1.entityId,
										bugEssence.max, bugEssence.current
									),
									p1 as EntityPlayerMP
								)
							} else {
								p1.sendMessage(
									TextComponentString(
										"This player can't store have bug essence"
									)
								)
							}
						}
						"item" -> {
							val bugEssence = BugEssenceCapability.isCapable(p1.heldItemMainhand)
							if (bugEssence != null) {
								when {
									p2[1] == fillArgument -> bugEssence.current = bugEssence.max
									p2[1].toIntOrNull() != null -> bugEssence.current = max(0, min(p2[1].toInt(), bugEssence.max))
									else -> p1.sendMessage(
										TextComponentString(
											"Second argument is not \"$fillArgument\" or an int"
										)
									)
								}
							} else {
								p1.sendMessage(
									TextComponentString(
										"This item can't store have bug essence"
									)
								)
							}
						}
						"chunk" -> {
							val bugEssence = BugEssenceCapability.isCapable(p1.world.getChunk(p1.position))
							if (bugEssence != null) {
								when {
									p2[1] == fillArgument -> bugEssence.current = bugEssence.max
									p2[1].toIntOrNull() != null -> bugEssence.current = max(0, min(p2[1].toInt(), bugEssence.max))
									else -> p1.sendMessage(
											TextComponentString(
												"Second argument is not \"$fillArgument\" or an int"
											)
										)
								}

								BugMagic.CHANNEL.sendTo(
									MessageChunkBugEssence(
										p1.position,
										bugEssence.max,
										bugEssence.current
									),
									p1 as EntityPlayerMP
								)
							}
						}
					}
				} else {
					p1.sendMessage(
						TextComponentString(
							"First argument is not a player"
						)
					)
				}
			}
		}
	}
}
