/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.common.networking.handler.HandlerBugEssence
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that contains; an entity ID, the maximum amount of bug essence they can have and their current bug essence.
 * @see [HandlerBugEssence]
 */
class MessageBugEssence(
        var entityID: Int,
        var max: Int,
        var current: Int
) : IMessage {
    @Suppress("unused")
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
