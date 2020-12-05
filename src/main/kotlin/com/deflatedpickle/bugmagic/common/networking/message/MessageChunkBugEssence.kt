/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.common.networking.handler.HandlerChunkBugEssence
import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that contains; an entity ID, the maximum amount of bug essence they can have and their current bug essence.
 * @see [HandlerChunkBugEssence]
 */
class MessageChunkBugEssence(
    private var blockPos: BlockPos? = null,
    private var max: Int = -1,
    private var current: Int = -1
) : IMessage {
    @Suppress("unused")
    constructor() : this(blockPos = null, max = -1, current = -1)

    operator fun component1() = this.blockPos
    operator fun component2() = this.max
    operator fun component3() = this.current

    override fun toBytes(buf: ByteBuf) {
		buf.writeLong(this.blockPos!!.toLong())
        buf.writeInt(this.max)
        buf.writeInt(this.current)
    }

    override fun fromBytes(buf: ByteBuf) {
        this.blockPos = BlockPos.fromLong(buf.readLong())
        this.max = buf.readInt()
        this.current = buf.readInt()
    }
}
