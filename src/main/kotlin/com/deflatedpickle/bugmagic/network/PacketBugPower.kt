package com.deflatedpickle.bugmagic.network

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.util.BugUtil
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketBugPower : IMessage, IMessageHandler<PacketBugPower, IMessage> {
    lateinit var player: EntityPlayer
    var bugPower: Int = 0
    var bugMaxPower: Int = 0

    constructor(playerIn: EntityPlayer) {
        this.player = playerIn
    }

    constructor()

    override fun fromBytes(buf: ByteBuf?) {
        this.bugPower = buf?.readInt()!!
        this.bugMaxPower = buf.readInt()
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.writeInt(BugUtil.getBugPower(this.player))
        buf?.writeInt(BugUtil.getMaxBugPower(this.player))
    }

    override fun onMessage(message: PacketBugPower, ctx: MessageContext?): IMessage? {
        BugUtil.setBugPower(BugMagic.proxy?.getPlayer(), message.bugPower)
        BugUtil.setMaxBugPower(BugMagic.proxy?.getPlayer(), message.bugMaxPower)

        return null
    }
}