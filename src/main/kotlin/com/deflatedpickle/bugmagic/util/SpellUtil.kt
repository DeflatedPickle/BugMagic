package com.deflatedpickle.bugmagic.util

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.network.PacketWand
import com.deflatedpickle.bugmagic.spells.SpellBase
import net.minecraft.item.ItemStack

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
}