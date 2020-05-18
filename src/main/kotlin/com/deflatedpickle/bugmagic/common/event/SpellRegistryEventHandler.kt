package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.api.spell.ASpell
import com.deflatedpickle.bugmagic.api.spell.ASpellRecipe
import com.deflatedpickle.bugmagic.common.init.Spell
import com.deflatedpickle.bugmagic.common.init.SpellRecipe
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @see [Spell]
 * @see [SpellRecipe]
 */
object SpellRegistryEventHandler {
    @SubscribeEvent
    fun onRegisterSpell(event: RegistryEvent.Register<ASpell>) {
        event.registry.registerAll(
                Spell.ITEM_COLLECTOR,
                Spell.ESSENCE_COLLECTOR,
                Spell.AUTO_HOE,
                Spell.AUTO_PLANTER,
                Spell.AUTO_FERTILIZER,
                Spell.AUTO_HARVESTER
        )
    }

    @SubscribeEvent
    fun onRegisterSpellRecipe(event: RegistryEvent.Register<ASpellRecipe>) {
        event.registry.registerAll(
                SpellRecipe.ITEM_COLLECTOR_RECIPE
        )
    }
}