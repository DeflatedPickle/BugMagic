package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.spell.Debug
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryBuilder

object Spell {
    val registry: IForgeRegistry<ASpell> = RegistryBuilder<ASpell>().setName(ResourceLocation(Reference.MOD_ID, "spell_registry")).setType(ASpell::class.java).create()

    val DEBUG = Debug

    init {
        registry.registerAll(DEBUG)
    }
}