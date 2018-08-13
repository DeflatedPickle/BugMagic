package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.util.BugUtil
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

    // TODO: Localize spell names in the lang file
    // The name of the spell
    var name: String = "base"
    var cult: String = "general"
    var id: Int = 0
    // The cost of the spell
    var cost: Int = 0
    // The amount of mana drained while the spell is cast
    var drain: Int = 0
    // The amount of time (in ticks) until the drain is applied
    var drainWait: Int = 0
    // The limit that can be casted (-1 = infinite, 0 = can't cast, > 1 = limited)
    var castLimit: Int = -1
    // The cooldown applied after the spell is cast
    var cooldownTime: Int = 0

    fun addToMap() {
        // Add the class to the spell map
        SpellUtil.spellMap[name] = this

        SpellUtil.nameToIDMap[name] = id
        SpellUtil.idToNameMap[id] = name
    }

    fun cast() {
        val casted = caster!!.entityData.getTag("bugmagic.casted") as NBTTagCompound?

        if (BugUtil.getBugPower(caster!!) - cost >= 0) {
            caster!!.cooldownTracker.setCooldown(caster!!.heldItemMainhand.item, cooldownTime)

            if (casted!!.getInteger(name) < castLimit) {
                BugUtil.useCappedBugPower(caster!!, cost)

                casted.setInteger(name, casted.getInteger(name) + 1)
                if (!caster!!.world.isRemote) {
                    limitedCast()
                }
            }
            else if (castLimit == -1) {
                BugUtil.useCappedBugPower(caster!!, cost)

                casted.setInteger(name, casted.getInteger(name) + 1)
                if (!caster!!.world.isRemote) {
                    unlimitedCast()
                }
            }
            else {
                caster!!.cooldownTracker.setCooldown(caster!!.heldItemMainhand.item, 0)

                if (!caster!!.world.isRemote) {
                    caster!!.sendStatusMessage(TextComponentString("Spell cast limit reached"), true)
                }
            }
        }
        else {
            if (!caster!!.world.isRemote) {
                caster!!.sendStatusMessage(TextComponentString("Not enough bug power"), true)
            }
        }
    }

    open fun uncast() {
        val casted = caster!!.entityData.getTag("bugmagic.casted") as NBTTagCompound?

        if (casted!!.getInteger(name) - 1 >= 0) {
            casted.setInteger(name, casted.getInteger(name) - 1)
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

            if (!caster!!.world.isRemote) {
                caster!!.sendStatusMessage(TextComponentString("Learnt the $name spell"), true)
            }
            parchment!!.shrink(1)
        }
    }
}