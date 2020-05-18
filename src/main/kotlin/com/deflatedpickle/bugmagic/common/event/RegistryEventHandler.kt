package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
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
        RegistryBuilder<com.deflatedpickle.bugmagic.api.spell.Spell>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        SpellInit.registry
                )
        ).setType(com.deflatedpickle.bugmagic.api.spell.Spell::class.java).create()

        RegistryBuilder<com.deflatedpickle.bugmagic.api.spell.SpellRecipe>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        SpellRecipeInit.recipe
                )
        ).setType(com.deflatedpickle.bugmagic.api.spell.SpellRecipe::class.java).create()
    }
}