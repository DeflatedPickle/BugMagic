/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.ASpell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class AutoFertilizer : ASpell() {
    override fun getName(): String = "Auto Fertilizer"
    override fun getManaLoss(): Int = 55
    override fun getCastCount(): Int = 1
    override fun getMaxCount(): Int = 4
    override fun getTier(): Tier = Tier.UNCOMMON
    override fun getCult(): Cult = Cult.APOIDEA

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoFertilizer::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoFertilizer::class.java, entityPlayer, itemWand)
    }
}
