/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.networking.handler

import com.deflatedpickle.bugmagic.client.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageSelectedSpell] packets
 */
class HandlerSelectedSpell : IMessageHandler<MessageSelectedSpell, IMessage> {
    override fun onMessage(message: MessageSelectedSpell, ctx: MessageContext): IMessage? {
        val (index) = message

        with(ctx.serverHandler.player) {
            val spellLearner = SpellLearnerCapability.isCapable(this)

            if (spellLearner != null) {
                spellLearner.currentIndex = index
            }
        }

        return null
    }
}
