/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.common.item.food.GenericFood
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraft.potion.PotionEffect

@AutoRegistry(Reference.MOD_ID)
object FoodInit {
    // Made by smelting a bug bundle
    @JvmField
    val PETRIFIED_BUG_BUNDLE = GenericFood(
            name = "petrified_bug_bundle",
            healAmount = 1,
            saturation = 0.1f,
            alwaysEdible = true,
            potionEffect = PotionEffect(PotionInit.BUG_ESSENCE_REGENERATION, 900),
            creativeEdible = true
    )
}
