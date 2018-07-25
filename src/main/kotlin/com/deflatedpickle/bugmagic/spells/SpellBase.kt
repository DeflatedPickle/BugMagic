package com.deflatedpickle.bugmagic.spells

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.TextComponentString

abstract class SpellBase {
    var caster: EntityPlayer? = null

    // The name of the spell
    var name: String = "base"
    // The cost of the spell
    var cost: Int = 0
    // The amount of mana drained while the spell is cast
    var drain: Int = 0
    // The amount of time (in ticks) until the drain is applied
    var drainWait: Int = 0

    abstract fun cast()

    open fun learn() {
        caster!!.sendStatusMessage(TextComponentString("Learnt the $name spell"), true)
    }
}