package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes

class Debug(val index: Int = 1) : ASpell() {
    override fun getName(): String = "Debug $index"
    override fun getManaLoss(): Int = 4
    override fun getCastingTime(): Int = 64

    override fun getTier(): Tier = Tier.DEBUG

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        println("Cast Debug $index!")
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        println("Uncast Debug $index!")
    }
}