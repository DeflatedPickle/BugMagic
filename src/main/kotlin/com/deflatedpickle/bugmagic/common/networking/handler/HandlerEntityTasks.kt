/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.networking.message.MessageEntityTasks
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageEntityTasks] packets
 */
class HandlerEntityTasks : IMessageHandler<MessageEntityTasks, IMessage> {
    override fun onMessage(message: MessageEntityTasks, ctx: MessageContext): IMessage? {
        val (entityID, taskEntries, executingTaskEntries) = message

        if (entityID != -1) {
            Minecraft.getMinecraft().addScheduledTask {
                with(Minecraft.getMinecraft().world.getEntityByID(entityID)) {
                    if (this is EntityCastable) {
                        this.clientTasks = taskEntries
                        this.clientExecutingTasks = executingTaskEntries
                    }
                }
            }
        }

        return null
    }
}
