/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessagePlayerBugEssence] packets
 */
class HandlerPlayerBugEssence : IMessageHandler<MessagePlayerBugEssence, IMessage> {
    override fun onMessage(message: MessagePlayerBugEssence, ctx: MessageContext): IMessage? {
        val (entityID, max, current) = message

        if (entityID != -1) {
            // Thanks Upcraft :v
            Minecraft.getMinecraft().addScheduledTask {
                Minecraft.getMinecraft().world.getEntityByID(entityID)?.let { entity ->
                    if (entity is EntityLivingBase) {
                        val bugEssence = BugEssenceCapability.isCapable(entity)

                        if (bugEssence != null) {
                            bugEssence.max = max
                            bugEssence.current = current
                        }
                    }
                }
            }
        }

        return null
    }
}
