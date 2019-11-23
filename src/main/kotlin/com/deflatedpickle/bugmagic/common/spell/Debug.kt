package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import net.minecraft.util.EnumParticleTypes

class Debug(val index: Int = 1) : ASpell() {
    init {
        this.setRegistryName("spell_debug_$index")
    }

    override fun getName(): String = "Debug $index"
    override fun getManaCost(): Int = 4
    override fun getCastingTime(): Int = 64
    override fun getMaxCooldown(): Int = 30

    override fun getTier(): Tier = Tier.DEBUG
    override fun getCastingShapePoints(): Int = 8
    override fun getCastingParticle(): EnumParticleTypes? = EnumParticleTypes.CRIT_MAGIC
    override fun getCancelingParticle(): EnumParticleTypes? = EnumParticleTypes.EXPLOSION_HUGE
    override fun getFinishingParticle(): EnumParticleTypes? = EnumParticleTypes.SWEEP_ATTACK

    override fun cast() {
        println("Cast Debug $index!")
    }

    override fun uncast() {
        println("Uncast Debug $index!")
    }
}