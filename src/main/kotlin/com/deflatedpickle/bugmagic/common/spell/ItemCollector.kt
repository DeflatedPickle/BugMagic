/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.common.init.SpellInit
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector as ItemCollectorMob
import java.util.ArrayList
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class ItemCollector : Spell() {
    class Recipe : SpellRecipe() {
        override fun getSpell(): Spell = SpellInit.ITEM_COLLECTOR

        override fun getIngredients(): ArrayList<ItemStack> {
            return arrayListOf(ItemStack(Items.SPIDER_EYE))
        }
    }

    override fun getName(): String = "Item Collector"
    override fun getManaLoss(): Int = 22
    override fun getCastCount(): Int = 1
    override fun getMaxCount(): Int = 3
    override fun getTier(): Tier = Tier.COMMON

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(ItemCollectorMob::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(ItemCollectorMob::class.java, entityPlayer, itemWand)
    }
}
