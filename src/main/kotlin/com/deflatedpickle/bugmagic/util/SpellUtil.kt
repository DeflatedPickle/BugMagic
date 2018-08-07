package com.deflatedpickle.bugmagic.util

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.network.PacketWand
import com.deflatedpickle.bugmagic.spells.SpellBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object SpellUtil {
    val spellMap: MutableMap<String, SpellBase> = hashMapOf()
    val nameToIDMap: MutableMap<String, Int> = hashMapOf()
    val idToNameMap: MutableMap<Int, String> = hashMapOf()

    fun getCurrentSpell(wandIn: ItemStack): String {
        return wandIn.tagCompound!!.getString("currentSpell")
    }

    fun setCurrentSpell(wandIn: ItemStack, value: String) {
        wandIn.tagCompound!!.setString("currentSpell", value)
        BugMagic.networkWrapper.sendToServer(PacketWand(wandIn))
    }

    fun uncastAllSpells(player: EntityPlayer) {
        if (!player.world.isRemote) {

            for (i in getCastedSpells(player)) {
                SpellUtil.spellMap[i]!!.uncast()
            }
        }
    }

    fun getPlayerSpells(player: EntityPlayer): List<String> {
        val list = mutableListOf<String>()

        for (i in (player.entityData.getTag("bugmagic.spells") as NBTTagCompound?)!!.keySet) {
            list.add(i)
        }

        return list
    }

    fun getCastedSpells(player: EntityPlayer): List<String> {
        val list = mutableListOf<String>()

        for (i in (player.entityData.getTag("bugmagic.casted") as NBTTagCompound?)!!.keySet) {
            list.add(i)
        }

        return list
    }
}