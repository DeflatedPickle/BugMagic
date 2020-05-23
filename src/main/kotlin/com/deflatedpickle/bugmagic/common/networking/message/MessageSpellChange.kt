/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.networking.handler.HandlerSpellChange
import io.netty.buffer.ByteBuf
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * A packet that contains; an entity ID, the maximum amount of bug essence they can have and their current bug essence.
 * @see [HandlerSpellChange]
 */
class MessageSpellChange(
    private var entityID: Int,
    private var spellList: List<Spell>
) : IMessage {
    @Suppress("unused")
    constructor() : this(entityID = 1, spellList = listOf())

    operator fun component1() = this.entityID
    operator fun component2() = this.spellList

    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeVarInt(buf, this.entityID, 5)

        buf.writeInt(this.spellList.size)
        for (i in this.spellList) {
            ByteBufUtils.writeUTF8String(buf, i.registryName.toString())
        }
    }

    override fun fromBytes(buf: ByteBuf) {
        this.entityID = ByteBufUtils.readVarInt(buf, 5)

        val list = mutableListOf<Spell>()
        val size = buf.readInt()

        for (i in 0 until size) {
            list.add(
                i,
                GameRegistry.findRegistry(Spell::class.java).getValue(
                    ResourceLocation(ByteBufUtils.readUTF8String(buf))
                )!!
            )
        }

        this.spellList = list
    }
}
