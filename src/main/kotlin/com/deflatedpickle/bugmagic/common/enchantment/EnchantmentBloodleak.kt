/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.enchantment

import com.deflatedpickle.bugmagic.api.common.enchantment.GenericEnchantment
import com.deflatedpickle.bugmagic.common.init.EnchantmentTypeInit
import net.minecraft.inventory.EntityEquipmentSlot

@Suppress("SpellCheckingInspection")
class EnchantmentBloodleak : GenericEnchantment(
    name = "bloodleak",
    rarity = Rarity.RARE,
    type = EnchantmentTypeInit.WAND,
    slots = arrayOf(
        EntityEquipmentSlot.MAINHAND
    )
)
