package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class HandlerBugEssence : IMessageHandler<MessageBugEssence, IMessage> {
    override fun onMessage(message: MessageBugEssence, ctx: MessageContext): IMessage? {
        if (BugEssence.Provider.CAPABILITY != null) {
            with(Minecraft.getMinecraft().player) {
                if (this.hasCapability(BugEssence.Provider.CAPABILITY!!, null)) {
                    this.getCapability(BugEssence.Provider.CAPABILITY!!, null)?.apply {
                        this.max = message.max
                        this.current = message.current
                    }
                }
            }
        }

        return null
    }
}