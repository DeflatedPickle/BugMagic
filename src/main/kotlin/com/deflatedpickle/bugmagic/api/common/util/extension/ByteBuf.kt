/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import com.deflatedpickle.bugmagic.api.common.util.AITaskString
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils

fun ByteBuf.writeEntityAITaskEntry(task: AITaskString) {
    ByteBufUtils.writeUTF8String(this, task.action)
    ByteBufUtils.writeVarInt(this, task.priority, 2)
    this.writeBoolean(task.using)
}

fun ByteBuf.readEntityAITaskEntry(): AITaskString {
    val name = ByteBufUtils.readUTF8String(this)
    val priority = ByteBufUtils.readVarInt(this, 2)
    val using = this.readBoolean()

    return AITaskString(priority, using, name)
}
