package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import net.minecraft.util.EnumParticleTypes
import java.awt.Shape
import java.awt.geom.Ellipse2D

object Debug : ASpell() {
    init {
        this.setRegistryName("spell_debug")
    }

    override fun getName(): String {
        return "Debug"
    }

    override fun getManaCost(): Int {
        return 10
    }

    override fun getTier(): Tier {
        return Tier.DEBUG
    }

    override fun getCastingShape(): Shape {
        return Ellipse2D.Float(0f, 0f, 4f, 4f)
    }

    override fun getCastingParticle(): EnumParticleTypes? {
        return EnumParticleTypes.BLOCK_DUST
    }

    override fun getFinishingParticle(): EnumParticleTypes? {
        return EnumParticleTypes.EXPLOSION_NORMAL
    }

    override fun cast() {}

    override fun uncast() {}
}