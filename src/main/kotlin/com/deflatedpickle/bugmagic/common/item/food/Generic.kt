package com.deflatedpickle.bugmagic.common.item.food

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

class Generic(name: String,
              private val useDuration: Int = 32,
              healAmount: Int,
              saturation: Float,
              isWolfFood: Boolean = false,
              alwaysEdible: Boolean = false,
              potionEffect: PotionEffect? = null,
              potionEffectProbability: Float = 1.0f)
    : ItemFood(healAmount, saturation, isWolfFood) {
    init {
        this.translationKey = name
        this.creativeTab = CreativeTabs.FOOD

        if (alwaysEdible) {
            this.setAlwaysEdible()
        }

        if (potionEffect != null) {
            setPotionEffect(potionEffect, potionEffectProbability)
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return this.useDuration
    }
}