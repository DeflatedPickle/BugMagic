/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.common.event.SpellRegistryEventHandler
import com.deflatedpickle.bugmagic.common.spell.*

/**
 * @see [SpellRegistryEventHandler]
 */
object Spell {
    val registry = "spell_recipe_registry"

    val DEBUG = mutableListOf<Debug>()

    val ITEM_COLLECTOR = ItemCollector()
    val ESSENCE_COLLECTOR = EssenceCollector()
    val AUTO_HOE = AutoHoe()
    val AUTO_PLANTER = AutoPlanter()
    val AUTO_FERTILIZER = AutoFertilizer()
    val AUTO_HARVESTER = AutoHarvester()
}
