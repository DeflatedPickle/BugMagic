/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellChange
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageSpellChange] packets
 */
class HandlerSpellChange : IMessageHandler<MessageSpellChange, IMessage> {
    override fun onMessage(message: MessageSpellChange, ctx: MessageContext): IMessage? {
        val (entityID, spellList) = message

        if (entityID != -1) {
            Minecraft.getMinecraft().addScheduledTask {
                Minecraft.getMinecraft().world.getEntityByID(entityID)?.let { entity ->
                    if (entity is EntityLivingBase) {
                        val spellLearner = SpellLearnerCapability.isCapable(entity)

                        if (spellLearner != null) {
                            spellLearner.spellList = spellList
                        }
                    }
                }
            }
        }

        return null
    }
}
