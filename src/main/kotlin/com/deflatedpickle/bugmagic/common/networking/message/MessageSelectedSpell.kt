/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessageSelectedSpell(var index: Int) : IMessage {
    constructor() : this(0)

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(index)
    }

    override fun fromBytes(buf: ByteBuf) {
        index = buf.readInt()
    }
}
