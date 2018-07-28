package com.deflatedpickle.bugmagic.network

import com.deflatedpickle.bugmagic.util.SpellUtil
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketWand : IMessage, IMessageHandler<PacketWand, IMessage> {
    lateinit var wand: ItemStack
    var currentSpell: Int = 0

    constructor(wandIn: ItemStack) {
        this.wand = wandIn
    }

    constructor()

    override fun fromBytes(buf: ByteBuf?) {
        this.currentSpell = buf?.readInt()!!
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.writeInt(SpellUtil.nameToIDMap[SpellUtil.getCurrentSpell(this.wand)]!!)
    }

    override fun onMessage(message: PacketWand, ctx: MessageContext?): IMessage? {
        ctx!!.serverHandler.player.heldItemMainhand.tagCompound!!.setString("currentSpell", SpellUtil.idToNameMap[message.currentSpell]!!)

        return null
    }
}