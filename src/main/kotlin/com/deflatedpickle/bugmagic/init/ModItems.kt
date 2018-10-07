package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.items.*
import com.deflatedpickle.bugmagic.spells.SpellBugpack
import com.deflatedpickle.bugmagic.spells.SpellFirefly
import com.deflatedpickle.bugmagic.spells.SpellWurm
import ladylib.registration.AutoRegister

@AutoRegister(Reference.MOD_ID, injectObjectHolder = true)
object ModItems {
    @JvmField
    val WAND_GENERIC = ItemWand().run { setMaxStackSize(1) }

    @JvmField
    val BUG_NET = ItemBugNet(1, 5)
    @JvmField
    val MAGNIFYING_GLASS = ItemMagnifyingGlass()

    // Spells
    @JvmField
    val SPELL_EMPTY = ItemSpellParchment(64, null)

    @JvmField
    val SPELL_FIREFLY = ItemSpellParchment(1, SpellFirefly())
    @JvmField
    val SPELL_BUGPACK = ItemSpellParchment(1, SpellBugpack())
    @JvmField
    val SPELL_WURM = ItemSpellParchment(1, SpellWurm())

    val spellList = listOf(SPELL_FIREFLY, SPELL_BUGPACK, SPELL_WURM)

    // Bug Parts
    @JvmField
    val BUG_PART_LEG  = ItemBugPart("leg")
    @JvmField
    val BUG_PART_WING = ItemBugPart("wing")
    @JvmField
    val BUG_PART_BODY = ItemBugPart("body")
    @JvmField
    val BUG_PART_HEAD = ItemBugPart("head")

    val bugParts = listOf<ItemBugPart>(
            BUG_PART_WING,
            BUG_PART_LEG,
            BUG_PART_BODY,
            BUG_PART_HEAD
    )

    @JvmField
    val BUG_JUICE = ItemBugJuice(25)
}