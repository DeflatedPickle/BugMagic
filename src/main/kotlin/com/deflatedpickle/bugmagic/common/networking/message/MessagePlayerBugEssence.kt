/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.common.networking.handler.HandlerPlayerBugEssence
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that contains; an entity ID, the maximum amount of bug essence they can have and their current bug essence.
 * @see [HandlerPlayerBugEssence]
 */
class MessagePlayerBugEssence(
    private var entityID: Int = 1,
    private var max: Int = -1,
    private var current: Int = -1
) : IMessage {
    @Suppress("unused")
    constructor() : this(entityID = 1, max = -1, current = -1)

    operator fun component1() = this.entityID
    operator fun component2() = this.max
    operator fun component3() = this.current

    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeVarInt(buf, this.entityID, 5)
        buf.writeInt(this.max)
        buf.writeInt(this.current)
    }

    override fun fromBytes(buf: ByteBuf) {
        this.entityID = ByteBufUtils.readVarInt(buf, 5)
        this.max = buf.readInt()
        this.current = buf.readInt()
    }
}
