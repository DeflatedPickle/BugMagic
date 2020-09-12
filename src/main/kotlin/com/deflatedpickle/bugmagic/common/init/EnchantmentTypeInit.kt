package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraftforge.common.util.EnumHelper

object EnchantmentTypeInit {
	val WAND = EnumHelper.addEnchantmentType("${Reference.MOD_ID}_wand") { it is Wand }!!
}
