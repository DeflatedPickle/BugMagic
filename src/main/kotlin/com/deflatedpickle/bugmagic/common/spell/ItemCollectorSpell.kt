/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.common.spell.SpellIngredient
import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import com.deflatedpickle.bugmagic.common.init.SpellInit
import java.util.ArrayList
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

/**
 * A spell that summons [ItemCollectorEntity]
 *
 * @author DeflatedPickle
 */
class ItemCollectorSpell : Spell() {
    class Recipe : SpellRecipe() {
        override fun getSpell(): Spell = SpellInit.ITEM_COLLECTOR

        override fun getIngredients(): ArrayList<SpellIngredient>? {
            return arrayListOf(
                    SpellIngredient(Items.SPIDER_EYE, 2)
            )
        }
    }

	init {
	    this.name = "Item Collector"
		this.manaLoss = 22
		this.castCount = 1
		this.maxCount = 3
		this.tier = Tier.COMMON
	}

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(ItemCollectorEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(ItemCollectorEntity::class.java, entityPlayer, itemWand)
    }
}
