package com.deflatedpickle.bugmagic.util

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.network.PacketBugPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

object BugUtil {
    fun getBugPower(playerIn: EntityPlayer): Int {
        return playerIn.entityData.getInteger("bugPower")
    }

    fun setBugPower(playerIn: EntityPlayer?, amount: Int?) {
        // Server
        if (playerIn is EntityPlayerMP) {
            BugMagic.logger.warn("Setting the Bug Power for %s on the server, ignoring the given amount (%d) and using the clients NBT".format(playerIn.displayName.formattedText, amount))
            playerIn.entityData.setInteger("bugPower", getBugPower(playerIn))
            BugMagic.networkWrapper.sendTo(PacketBugPower(playerIn), playerIn)
        }
        // Client
        else {
            BugMagic.logger.info("Setting the Bug Power for %s on the client to %d".format(playerIn?.displayName?.formattedText, amount))
            playerIn?.entityData?.setInteger("bugPower", amount!!)
        }
    }

    fun useBugPower(playerIn: EntityPlayer, amount: Int?) {
        setBugPower(playerIn, getBugPower(playerIn) - amount!!)
    }
}