package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.util.SpellUtil
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.text.TextComponentString

abstract class SpellBase {
    var caster: EntityPlayer? = null
    var parchment: ItemStack? = null

    // The name of the spell
    var name: String = "base"
    var id: Int = 0
    // The cost of the spell
    var cost: Int = 0
    // The amount of mana drained while the spell is cast
    var drain: Int = 0
    // The amount of time (in ticks) until the drain is applied
    var drainWait: Int = 0
    // The limit that can be casted (-1 = infinite, 0 = can't cast, > 1 = limited)
    var castLimit: Int = -1

    fun addToMap() {
        // Add the class to the spell map
        SpellUtil.spellMap[name] = this

        SpellUtil.nameToIDMap[name] = id
        SpellUtil.idToNameMap[id] = name
    }

    open fun cast() {
        val casted = caster!!.entityData.getTag("bugmagic.casted") as NBTTagCompound?

        if (casted!!.getInteger(name) < castLimit || castLimit == -1) {
            casted.setInteger(name, casted.getInteger(name) + 1)
            limitedCast()
        }
        else {
            casted.setInteger(name, 1)
            unlimitedCast()
        }
    }

    open fun limitedCast() {}
    open fun unlimitedCast() {}

    open fun learn() {
        val spells = caster!!.entityData.getTag("bugmagic.spells") as NBTTagCompound?

        if (spells!!.getBoolean(name)) {
            caster!!.sendStatusMessage(TextComponentString("You have already learnt that spell"), true)
        }
        else {
            spells.setBoolean(name, true)

            if (caster is EntityPlayerSP) {
                caster!!.sendStatusMessage(TextComponentString("Learnt the $name spell"), true)
            }
            parchment!!.shrink(1)
        }
    }
}