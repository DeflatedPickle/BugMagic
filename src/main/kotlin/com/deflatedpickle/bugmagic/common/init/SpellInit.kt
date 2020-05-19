/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.event.SpellRegistryEventHandler
import com.deflatedpickle.bugmagic.common.spell.AutoFertilizerSpell
import com.deflatedpickle.bugmagic.common.spell.AutoHarvesterSpell
import com.deflatedpickle.bugmagic.common.spell.AutoHoeSpell
import com.deflatedpickle.bugmagic.common.spell.AutoPlanterSpell
import com.deflatedpickle.bugmagic.common.spell.EssenceCollectorSpell
import com.deflatedpickle.bugmagic.common.spell.ItemCollectorSpell
import net.minecraftforge.registries.IForgeRegistry

/**
 * @see [SpellRegistryEventHandler]
 */
object SpellInit {
    val registryKey = "spell_recipe_registry"
    lateinit var registry: IForgeRegistry<Spell>

    val ITEM_COLLECTOR = ItemCollectorSpell()
    val ESSENCE_COLLECTOR = EssenceCollectorSpell()
    val AUTO_HOE = AutoHoeSpell()
    val AUTO_PLANTER = AutoPlanterSpell()
    val AUTO_FERTILIZER = AutoFertilizerSpell()
    val AUTO_HARVESTER = AutoHarvesterSpell()
}
