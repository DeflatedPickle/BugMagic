package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import net.minecraft.util.EnumParticleTypes
import kotlin.random.Random

class Debug(val index: Int = 1) : ASpell() {
    init {
        this.setRegistryName("spell_debug_$index")
    }

    override fun getName(): String {
        return "Debug $index"
    }

    override fun getManaCost(): Int {
        return Random.nextInt(4, 40)
    }

    override fun getTier(): Tier {
        return Tier.DEBUG
    }

    override fun getCastingShapePoints(): Int {
        return Random.nextInt(3, 360)
    }

    override fun getCastingParticle(): EnumParticleTypes? {
        return EnumParticleTypes.BLOCK_DUST
    }

    override fun getFinishingParticle(): EnumParticleTypes? {
        return EnumParticleTypes.EXPLOSION_NORMAL
    }

    override fun cast() {
        println("Cast Debug $index!")
    }

    override fun uncast() {
        println("Uncast Debug $index!")
    }
}