/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.common.spell.ItemCollectorSpell
import net.minecraftforge.registries.IForgeRegistry

object SpellRecipeInit {
    val registryKey = "spell_registry"
    lateinit var registry: IForgeRegistry<SpellRecipe>

    val ITEM_COLLECTOR_RECIPE = ItemCollectorSpell.Recipe()
}
