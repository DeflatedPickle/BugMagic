package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumParticleTypes

class Debug(val index: Int = 1) : ASpell() {
    init {
        this.setRegistryName("spell_debug_$index")
    }

    override fun getName(): String = "Debug $index"
    override fun getManaCost(): Int = 4
    override fun getCastingTime(): Int = 64

    override fun getTier(): Tier = Tier.DEBUG

    override fun cast(entityPlayer: EntityPlayer) {
        println("Cast Debug $index!")
    }

    override fun uncast(entityPlayer: EntityPlayer) {
        println("Uncast Debug $index!")
    }
}