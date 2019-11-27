package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.spell.Debug
import com.deflatedpickle.bugmagic.common.spell.ItemCollector
import net.minecraft.launchwrapper.Launch
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryBuilder

object Spell {
    val registry: IForgeRegistry<ASpell> = RegistryBuilder<ASpell>().setName(ResourceLocation(Reference.MOD_ID, "spell_registry")).setType(ASpell::class.java).create()

    val DEBUG = mutableListOf<Debug>()

    val ITEM_COLLECTOR = ItemCollector()

    init {
        if (Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean) {
            this.DEBUG.addAll(arrayOf(Debug(), Debug(2), Debug(3), Debug(4)))
        }
        registry.registerAll(*DEBUG.toTypedArray())

        registry.register(ITEM_COLLECTOR)
    }
}