/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageChunkBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.chunk.EmptyChunk
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageChunkBugEssence] packets
 */
class HandlerChunkBugEssence : IMessageHandler<MessageChunkBugEssence, IMessage> {
    override fun onMessage(message: MessageChunkBugEssence, ctx: MessageContext): IMessage? {
        val (blockPos, max, current) = message

        if (blockPos != null) {
            Minecraft.getMinecraft().addScheduledTask {
                val chunk = Minecraft.getMinecraft().world.getChunk(blockPos)

				if (chunk !is EmptyChunk) {
					val bugEssence = BugEssenceCapability.isCapable(chunk)

					if (bugEssence != null) {
						bugEssence.max = max
						bugEssence.current = current
					}
				}
            }
        }

        return null
    }
}
