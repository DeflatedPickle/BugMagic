package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.items.*
import com.deflatedpickle.bugmagic.spells.SpellBugpack
import com.deflatedpickle.bugmagic.spells.SpellFirefly

object ModItems {
    val wandGeneric = ItemWand("wand_generic", 1, ModCreativeTabs.tabGeneral)

    val bugNet = ItemBugNet("bug_net", 1, ModCreativeTabs.tabGeneral, 5)
    val magnifyingGlass = ItemMagnifyingGlass("magnifying_glass")

    // Spells
    val spellFirefly = ItemSpellParchment("spell_firefly", 1, ModCreativeTabs.tabGeneral, SpellFirefly())
    val spellBugpack = ItemSpellParchment("spell_bugpack", 1, ModCreativeTabs.tabGeneral, SpellBugpack())

    val spellList = listOf(spellFirefly, spellBugpack)

    // Bug Parts
    val partLeg = ItemBugPart("bug_part_leg", "leg")
    val partWing = ItemBugPart("bug_part_wing", "wing")
    val partBody = ItemBugPart("bug_part_body", "body")
    val partHead = ItemBugPart("bug_part_head", "head")

    val bugParts = listOf<ItemBugPart>(
            partWing,
            partLeg,
            partBody,
            partHead
    )

    val bugJuice = ItemBugJuice("bug_juice", 25)
}