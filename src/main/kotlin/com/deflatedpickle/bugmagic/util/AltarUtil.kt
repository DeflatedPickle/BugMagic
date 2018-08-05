package com.deflatedpickle.bugmagic.util

import net.minecraft.item.Item

object AltarUtil {
    // TODO: Add a way to set bug resonance cost for recipes
    private val recipeMap = mutableMapOf<List<Item>, Item>()

    fun getRecipes(): Map<List<Item>, Item> {
        return recipeMap
    }

    fun addRecipe(result: Item, items: List<Item>) {
        recipeMap[items] = result
    }
}