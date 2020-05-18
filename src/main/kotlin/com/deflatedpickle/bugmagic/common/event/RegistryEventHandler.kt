package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.spell.ASpell
import com.deflatedpickle.bugmagic.api.spell.ASpellRecipe
import com.deflatedpickle.bugmagic.common.init.Spell
import com.deflatedpickle.bugmagic.common.init.SpellRecipe
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.registries.RegistryBuilder

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object RegistryEventHandler {
    @SubscribeEvent
    fun onRegisterEventNewRegistry(event: RegistryEvent.NewRegistry) {
        RegistryBuilder<ASpell>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        Spell.registry
                )
        ).setType(ASpell::class.java).create()

        RegistryBuilder<ASpellRecipe>().setName(
                ResourceLocation(
                        Reference.MOD_ID,
                        SpellRecipe.recipe
                )
        ).setType(ASpellRecipe::class.java).create()
    }
}