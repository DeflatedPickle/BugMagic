/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that contains; an entity ID, their casting state and their casting progress
 * @see [MessageSpellCaster]
 */
class MessageSpellCaster(
        private var entityID: Int,
        private var isCasting: Boolean,
        private var castingCurrent: Float
) : IMessage {
    @Suppress("unused")
    constructor() : this(entityID = 1, isCasting = false, castingCurrent = -1f)

    operator fun component1() = this.entityID
    operator fun component2() = this.isCasting
    operator fun component3() = this.castingCurrent

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
