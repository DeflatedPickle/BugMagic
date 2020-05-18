/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssence
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
        with(Minecraft.getMinecraft().world.getEntityByID(message.entityID)) {
            if (this is EntityLivingBase) {
                val bugEssence = BugEssence.isCapable(this)

                if (bugEssence != null) {
                    bugEssence.max = message.max
                    bugEssence.current = message.current
                }
            }
        }

        return null
    }
}
