/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import com.deflatedpickle.bugmagic.api.common.spell.SpellIngredient
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import net.minecraft.item.ItemStack

fun SpellRecipe.containsExact(input: Collection<ItemStack>): Boolean =
        input.none { SpellIngredient(it.item, it.count) !in this.ingredients!! }

fun SpellRecipe.containsAll(input: Collection<ItemStack>): Boolean =
        input.all { SpellIngredient(it.item, it.count) in this.ingredients!! }

fun SpellRecipe.getAll(input: MutableCollection<ItemStack>): Collection<ItemStack> =
        input.filter { SpellIngredient(it.item, it.count) in this.ingredients!! }

fun SpellRecipe.removeAll(input: MutableCollection<ItemStack>): Collection<ItemStack> =
        input.also { ingredient ->
            ingredient.removeIf {
                SpellIngredient(it.item, it.count) in this.ingredients!!
            }
        }
