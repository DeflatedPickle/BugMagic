package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessageBugEssence(var entityID: Int, var max: Int, var current: Int) : IMessage {
    constructor() : this(1, -1, -1)

    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeVarInt(buf, entityID, 5)
        buf.writeInt(max)
        buf.writeInt(current)
    }

    override fun fromBytes(buf: ByteBuf) {
        entityID = ByteBufUtils.readVarInt(buf, 5)
        max = buf.readInt()
        current = buf.readInt()
    }
}