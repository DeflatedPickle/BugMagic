/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [AutoFertilizerEntity]
 *
 * @author DeflatedPickle
 */
class AutoFertilizerSpell : Spell() {
	init {
	    this.name = "Auto Fertilizer"
		this.manaLoss = 55
		this.castCount = 1
		this.maxCount = 1
		this.tier = Tier.UNCOMMON
		this.cult = Cult.APOIDEA
	}

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoFertilizerEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoFertilizerEntity::class.java, entityPlayer, itemWand)
    }
}
