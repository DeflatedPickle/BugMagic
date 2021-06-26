@file:Suppress("SpellCheckingInspection", "MemberVisibilityCanBePrivate", "CanBeParameter")

package com.deflatedpickle.bugmagic.api.common.material

import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemArmor
import net.minecraft.util.SoundEvent
import net.minecraftforge.common.util.EnumHelper

/**
 * @see net.minecraft.item.ItemArmor.ArmorMaterial
 */
open class GenericArmourMaterial(
	val name: String,
	val texture: String = "",
	val durability: Int = 100,
	val reductionAmounts: ReductionAmounts = ReductionAmounts(),
	val enchantability: Int = 0,
	val soundOnEquip: SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
	val toughness: Float = 0f
) {
	data class ReductionAmounts(
		val helmet: Int = 0,
		val chestplate: Int = 0,
		val leggings: Int = 0,
		val boots: Int = 0
	)

	private val enumValue = EnumHelper.addArmorMaterial(
		name,
		texture,
		durability,
		intArrayOf(
			reductionAmounts.helmet,
			reductionAmounts.chestplate,
			reductionAmounts.leggings,
			reductionAmounts.boots
		),
		enchantability,
		soundOnEquip,
		toughness
	)

	fun get(): ItemArmor.ArmorMaterial = enumValue!!
}
