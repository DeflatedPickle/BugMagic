/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessageSpellCaster(var entityID: Int, var isCasting: Boolean, var castingCurrent: Float) : IMessage {
    constructor() : this(1, false, -1f)

    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeVarInt(buf, entityID, 5)
        buf.writeBoolean(isCasting)
        buf.writeFloat(castingCurrent)
    }

    override fun fromBytes(buf: ByteBuf) {
        entityID = ByteBufUtils.readVarInt(buf, 5)
        isCasting = buf.readBoolean()
        castingCurrent = buf.readFloat()
    }
}
