package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import java.nio.charset.Charset
import java.util.*

class MessageSpellCaster(var isCasting: Boolean, var castingCurrent: Float) : IMessage {
    constructor() : this(false, 0f)

    override fun toBytes(buf: ByteBuf) {
        buf.writeBoolean(isCasting)
        buf.writeFloat(castingCurrent)
    }

    override fun fromBytes(buf: ByteBuf) {
        isCasting = buf.readBoolean()
        castingCurrent = buf.readFloat()
    }
}