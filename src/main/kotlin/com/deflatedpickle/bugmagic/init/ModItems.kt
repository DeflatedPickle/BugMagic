package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.items.ItemBugNet
import com.deflatedpickle.bugmagic.items.ItemSpellParchment
import com.deflatedpickle.bugmagic.items.ItemWand
import com.deflatedpickle.bugmagic.spells.SpellBugpack
import com.deflatedpickle.bugmagic.spells.SpellFirefly

object ModItems {
    val wandGeneric = ItemWand("wand_generic", 1, ModCreativeTabs.tabGeneral)

    val bugNet = ItemBugNet("bug_net", 1, ModCreativeTabs.tabGeneral, 5)

    // Spells
    val spellFirefly = ItemSpellParchment("spell_firefly", 1, ModCreativeTabs.tabGeneral, SpellFirefly())
    val spellBugpack = ItemSpellParchment("spell_bugpack", 1, ModCreativeTabs.tabGeneral, SpellBugpack())
}