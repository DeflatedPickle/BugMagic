package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.SpellLearner
import com.deflatedpickle.bugmagic.common.networking.message.MessageSelectedSpell
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class HandlerSelectedSpell : IMessageHandler<MessageSelectedSpell, IMessage> {
    override fun onMessage(message: MessageSelectedSpell, ctx: MessageContext): IMessage? {
        if (SpellLearner.Provider.CAPABILITY != null) {
            with(ctx.serverHandler.player) {
                val spellLearner = SpellLearner.isCapable(this)

                if (spellLearner != null) {
                    spellLearner.currentIndex = message.index
                }
            }
        }

        return null
    }
}