package com.deflatedpickle.bugmagic.util

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.network.PacketBugPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

object BugUtil {
    fun getMaxBugPower(playerIn: EntityPlayer): Int {
        return playerIn.entityData.getInteger("bugMaxPower")
    }

    fun setMaxBugPower(playerIn: EntityPlayer?, amount: Int?) {
        // // Server
        // if (playerIn is EntityPlayerMP) {
        //     playerIn.entityData.setInteger("bugMaxPower", getBugPower(playerIn))
        //     BugMagic.networkWrapper.sendTo(PacketBugPower(playerIn), playerIn)
        // }
        // // Client
        // else {
        //     playerIn?.entityData?.setInteger("bugMaxPower", amount!!)
        // }
        playerIn?.entityData?.setInteger("bugMaxPower", amount!!)
        BugMagic.networkWrapper.sendToServer(PacketBugPower(playerIn!!))
    }

    fun getBugPower(playerIn: EntityPlayer): Int {
        return playerIn.entityData.getInteger("bugPower")
    }

    fun setBugPower(playerIn: EntityPlayer?, amount: Int?) {
        // // Server
        // if (playerIn is EntityPlayerMP) {
        //     BugMagic.logger.warn("Setting the Bug Power for %s on the server, ignoring the given amount (%d) and using the clients NBT".format(playerIn.displayName.formattedText, amount))
        //     playerIn.entityData.setInteger("bugPower", getBugPower(playerIn))
        //     BugMagic.networkWrapper.sendTo(PacketBugPower(playerIn), playerIn)
        // }
        // // Client
        // else {
        //     BugMagic.logger.info("Setting the Bug Power for %s on the client to %d".format(playerIn?.displayName?.formattedText, amount))
        //     playerIn?.entityData?.setInteger("bugPower", amount!!)
        // }
        playerIn?.entityData?.setInteger("bugPower", amount!!)
        BugMagic.networkWrapper.sendToServer(PacketBugPower(playerIn!!))
    }

    fun useBugPower(playerIn: EntityPlayer, amount: Int?) {
        setBugPower(playerIn, getBugPower(playerIn) - amount!!)
    }

    fun useCappedBugPower(playerIn: EntityPlayer, amount: Int?) {
        if (getBugPower(playerIn) - amount!! >= 0) {
            useBugPower(playerIn, amount)
        }
    }

    fun giveBugPower(playerIn: EntityPlayer, amount: Int?) {
        setBugPower(playerIn, getBugPower(playerIn) + amount!!)
    }

    fun giveCappedBugPower(playerIn: EntityPlayer, amount: Int?) {
        if (getBugPower(playerIn) + amount!! <= getMaxBugPower(playerIn)) {
            giveBugPower(playerIn, amount)
        }
    }
}