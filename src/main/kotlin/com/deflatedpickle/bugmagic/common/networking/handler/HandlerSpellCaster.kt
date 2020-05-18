/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.handler

import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
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
        val (entityID, isCasting, castingCurrent) = message

        with(Minecraft.getMinecraft().world.getEntityByID(entityID)) {
            if (this is EntityLivingBase) {
                val spellCaster = SpellCasterCapability.isCapable(this.heldItemMainhand)

                if (spellCaster != null) {
                    spellCaster.isCasting = isCasting
                    spellCaster.castingCurrent = castingCurrent
                }
            }
        }

        return null
    }
}
