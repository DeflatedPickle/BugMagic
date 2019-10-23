package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.BugEssence
import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class HandlerSpellCaster : IMessageHandler<MessageSpellCaster, IMessage> {
    override fun onMessage(message: MessageSpellCaster, ctx: MessageContext): IMessage? {
        if (BugEssence.Provider.CAPABILITY != null) {
            with(Minecraft.getMinecraft().player.heldItemMainhand) {
                val spellCaster = SpellCaster.isCapable(this)

                if (spellCaster != null) {
                    spellCaster.isCasting = message.isCasting
                    spellCaster.castingCurrent = message.castingCurrent
                }
            }
        }

        return null
    }
}