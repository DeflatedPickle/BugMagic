/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.common.init.SpellInit
import com.deflatedpickle.bugmagic.common.init.SpellRecipeInit
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.registries.RegistryBuilder

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object RegistryEventHandler {
    @SubscribeEvent
    @JvmStatic
    fun onRegisterEventNewRegistry(event: RegistryEvent.NewRegistry) {
        SpellInit.registry = RegistryBuilder<Spell>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        SpellInit.registryKey
                )
        ).setType(Spell::class.java).create()

        SpellRecipeInit.registry = RegistryBuilder<SpellRecipe>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        SpellRecipeInit.registryKey
                )
        ).setType(SpellRecipe::class.java).create()
    }
}
