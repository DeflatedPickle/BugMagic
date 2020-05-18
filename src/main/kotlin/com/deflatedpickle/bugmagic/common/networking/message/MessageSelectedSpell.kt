/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSelectedSpell
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that stores the index of the currently selected spell for an entity
 * @see [HandlerSelectedSpell]
 */
class MessageSelectedSpell(
        private var index: Int = -1
) : IMessage {
    @Suppress("unused")
    constructor() : this(index = -1)

    operator fun component1() = this.index

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(index)
    }

    override fun fromBytes(buf: ByteBuf) {
        index = buf.readInt()
    }
}
