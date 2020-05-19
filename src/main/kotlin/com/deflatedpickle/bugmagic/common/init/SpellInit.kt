/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.common.event.SpellRegistryEventHandler
import com.deflatedpickle.bugmagic.common.spell.*

/**
 * @see [SpellRegistryEventHandler]
 */
object SpellInit {
    val registry = "spell_recipe_registry"

    val ITEM_COLLECTOR = ItemCollectorSpell()
    val ESSENCE_COLLECTOR = EssenceCollectorSpell()
    val AUTO_HOE = AutoHoeSpell()
    val AUTO_PLANTER = AutoPlanterSpell()
    val AUTO_FERTILIZER = AutoFertilizerSpell()
    val AUTO_HARVESTER = AutoHarvesterSpell()
}
