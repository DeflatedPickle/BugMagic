package com.deflatedpickle.bugmagic.api.common.enchantment

import com.deflatedpickle.bugmagic.Reference
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation

open class GenericEnchantment(
	name: String,
	rarity: Rarity,
	type: EnumEnchantmentType,
	slots: Array<EntityEquipmentSlot>,
	private val isAllowedOnBooks: Boolean = true,
	private val isCurse: Boolean = false,
	private val isTreasureEnchantment: Boolean = false,
	private val minLevel: Int = 1,
	private val maxLevel: Int = 1
) : Enchantment(rarity, type, slots) {
	init {
		this.setRegistryName(Reference.MOD_ID, name)
		this.setName(name)
	}

	override fun isAllowedOnBooks(): Boolean = this.isAllowedOnBooks
	override fun isCurse(): Boolean = this.isCurse
	override fun isTreasureEnchantment(): Boolean = this.isTreasureEnchantment
	override fun getMinLevel(): Int = this.minLevel
	override fun getMaxLevel(): Int = this.maxLevel
}
