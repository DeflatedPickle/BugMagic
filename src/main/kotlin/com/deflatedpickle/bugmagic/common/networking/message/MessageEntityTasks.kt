/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.networking.message

import com.deflatedpickle.bugmagic.api.common.util.AITaskString
import com.deflatedpickle.bugmagic.api.common.util.extension.readEntityAITaskEntry
import com.deflatedpickle.bugmagic.api.common.util.extension.writeEntityAITaskEntry
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * A packet that contains; an entity ID, their casting state and their casting progress
 * @see [MessageEntityTasks]
 */
class MessageEntityTasks(
    private var entityID: Int,
    private var taskEntries: Set<AITaskString>,
    private var executingTaskEntries: Set<AITaskString>
) : IMessage {
    @Suppress("unused")
    constructor() : this(entityID = 1, taskEntries = setOf(), executingTaskEntries = setOf())

    operator fun component1() = this.entityID
    operator fun component2() = this.taskEntries
    operator fun component3() = this.executingTaskEntries

    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeVarInt(buf, this.entityID, 4)

        buf.writeInt(this.taskEntries.size)
        for (i in this.taskEntries.iterator()) {
            buf.writeEntityAITaskEntry(i)
        }

        buf.writeInt(this.executingTaskEntries.size)
        for (i in this.executingTaskEntries.iterator()) {
            buf.writeEntityAITaskEntry(i)
        }
    }

    override fun fromBytes(buf: ByteBuf) {
        this.entityID = ByteBufUtils.readVarInt(buf, 4)

        val entrySet = mutableSetOf<AITaskString>()
        val entryLength = buf.readInt()
        for (i in 0 until entryLength) {
            entrySet.add(buf.readEntityAITaskEntry())
        }
        this.taskEntries = entrySet

        val executingSet = mutableSetOf<AITaskString>()
        val executingLength = buf.readInt()
        for (i in 0 until executingLength) {
            executingSet.add(buf.readEntityAITaskEntry())
        }
        this.executingTaskEntries = executingSet
    }
}
