/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [EssenceCollectorEntity]
 *
 * @author DeflatedPickle
 */
class EssenceCollectorSpell : Spell() {
	init {
	    this.name = "Essence Collector"
		this.manaLoss = 40
		this.castCount = 2
		this.maxCount = 2
		this.tier = Tier.RARE
		this.cult = Cult.APOIDEA
	}

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(EssenceCollectorEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(EssenceCollectorEntity::class.java, entityPlayer, itemWand)
    }
}
