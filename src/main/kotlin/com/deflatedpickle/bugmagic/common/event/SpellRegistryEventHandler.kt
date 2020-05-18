package com.deflatedpickle.bugmagic.common.event

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.common.init.SpellInit
import com.deflatedpickle.bugmagic.common.init.SpellRecipeInit
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @see [Spell]
 * @see [SpellRecipe]
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object SpellRegistryEventHandler {
    @SubscribeEvent
    @JvmStatic
    fun onRegisterSpell(event: RegistryEvent.Register<Spell>) {
        event.registry.registerAll(
                SpellInit.ITEM_COLLECTOR,
                SpellInit.ESSENCE_COLLECTOR,
                SpellInit.AUTO_HOE,
                SpellInit.AUTO_PLANTER,
                SpellInit.AUTO_FERTILIZER,
                SpellInit.AUTO_HARVESTER
        )
    }

    @SubscribeEvent
    @JvmStatic
    fun onRegisterSpellRecipe(event: RegistryEvent.Register<SpellRecipe>) {
        event.registry.registerAll(
                SpellRecipeInit.ITEM_COLLECTOR_RECIPE
        )
    }
}