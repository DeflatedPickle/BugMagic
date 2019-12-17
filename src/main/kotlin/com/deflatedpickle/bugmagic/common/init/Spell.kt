/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.spell.ASpell
import com.deflatedpickle.bugmagic.common.spell.AutoFertilizer
import com.deflatedpickle.bugmagic.common.spell.AutoHarvester
import com.deflatedpickle.bugmagic.common.spell.AutoHoe
import com.deflatedpickle.bugmagic.common.spell.AutoPlanter
import com.deflatedpickle.bugmagic.common.spell.Debug
import com.deflatedpickle.bugmagic.common.spell.EssenceCollector
import com.deflatedpickle.bugmagic.common.spell.ItemCollector
import net.minecraft.launchwrapper.Launch
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryBuilder

object Spell {
    val registry: IForgeRegistry<ASpell> = RegistryBuilder<ASpell>().setName(
            ResourceLocation(Reference.MOD_ID,
                    "spell_registry")
    ).setType(ASpell::class.java).create()

    val DEBUG = mutableListOf<Debug>()

    val ITEM_COLLECTOR = ItemCollector()
    val ESSENCE_COLLECTOR = EssenceCollector()
    val AUTO_HOE = AutoHoe()
    val AUTO_PLANTER = AutoPlanter()
    val AUTO_FERTILIZER = AutoFertilizer()
    val AUTO_HARVESTER = AutoHarvester()

    init {
        if (Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean) {
            this.DEBUG.addAll(arrayOf(Debug(), Debug(2), Debug(3), Debug(4)))
        }
        registry.registerAll(*DEBUG.toTypedArray())

        registry.register(ITEM_COLLECTOR)
        registry.register(ESSENCE_COLLECTOR)
        registry.register(AUTO_HOE)
        registry.register(AUTO_PLANTER)
        registry.register(AUTO_FERTILIZER)
        registry.register(AUTO_HARVESTER)
    }
}
