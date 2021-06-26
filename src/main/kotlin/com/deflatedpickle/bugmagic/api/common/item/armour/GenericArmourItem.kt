@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

package com.deflatedpickle.bugmagic.api.common.item.armour

import com.deflatedpickle.bugmagic.api.common.material.GenericArmourMaterial
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

open class GenericArmourItem(
	creativeTab: CreativeTabs? = CreativeTabs.COMBAT,
	canRepair: Boolean = true,
	stackLimit: Int = 64,
	private val isDamageable: Boolean = true,
	private val isMap: Boolean = false,
	private val itemEnchantability: Int = 0,
	private val isRepairable: Boolean = false,
	private val showDurabilityBar: Boolean = false,
	private val maxDamage: Int = 0,
	private val actionItemUse: EnumAction = EnumAction.NONE,
	private val itemUseDuration: Int = 0,
	material: GenericArmourMaterial,
	renderIndex: Int = 0,
	equipmentSlot: EntityEquipmentSlot
) : ItemArmor(
	material.get(),
	renderIndex,
	equipmentSlot
) {
	init {
		this.translationKey = "${material.name}_${equipmentSlot.getName()}"
		this.creativeTab = creativeTab

		this.canRepair = canRepair
		this.maxStackSize = stackLimit
	}

	override fun isDamageable(): Boolean = this.isDamageable
	override fun isMap(): Boolean = this.isMap
	override fun getItemEnchantability(): Int = this.itemEnchantability
	override fun isRepairable(): Boolean = this.isRepairable
	override fun showDurabilityBar(stack: ItemStack): Boolean = this.showDurabilityBar
	override fun getMaxDamage(): Int = this.maxDamage

	override fun getItemUseAction(stack: ItemStack): EnumAction = this.actionItemUse
	override fun getMaxItemUseDuration(stack: ItemStack): Int = this.itemUseDuration
}
