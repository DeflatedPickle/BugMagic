/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageBugEssence] packets
 */
class HandlerBugEssence : IMessageHandler<MessageBugEssence, IMessage> {
    override fun onMessage(message: MessageBugEssence, ctx: MessageContext): IMessage? {
        val (entityID, max, current) = message

        if (entityID != -1) {
            with(Minecraft.getMinecraft().world.getEntityByID(entityID)) {
                if (this is EntityLivingBase) {
                    val bugEssence = BugEssenceCapability.isCapable(this)

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
