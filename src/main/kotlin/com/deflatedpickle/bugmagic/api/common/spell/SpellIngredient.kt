/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.spell

import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import net.minecraft.item.Item

/**
 * An ingredient in a [SpellRecipe]
 */
data class SpellIngredient(val item: Item, val count: Int)
