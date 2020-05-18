/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.SpellCaster
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The handler for [MessageSpellCaster] packets
 */
class HandlerSpellCaster : IMessageHandler<MessageSpellCaster, IMessage> {
    override fun onMessage(message: MessageSpellCaster, ctx: MessageContext): IMessage? {
        with(Minecraft.getMinecraft().world.getEntityByID(message.entityID)) {
            if (this is EntityLivingBase) {
                val spellCaster = SpellCaster.isCapable(this.heldItemMainhand)

                if (spellCaster != null) {
                    spellCaster.isCasting = message.isCasting
                    spellCaster.castingCurrent = message.castingCurrent
                }
            }
        }

        return null
    }
}
