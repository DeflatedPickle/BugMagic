package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessageBugEssence(var max: Int, var current: Int) : IMessage {
    constructor() : this(0, 0)

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(max)
        buf.writeInt(current)
    }

    override fun fromBytes(buf: ByteBuf) {
        max = buf.readInt()
        current = buf.readInt()
    }
}