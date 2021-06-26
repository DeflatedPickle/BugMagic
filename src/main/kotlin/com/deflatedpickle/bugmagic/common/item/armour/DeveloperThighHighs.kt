package com.deflatedpickle.bugmagic.common.item.armour

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.common.item.armour.GenericArmourItem
import com.deflatedpickle.bugmagic.api.common.material.GenericArmourMaterial
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation

class DeveloperThighHighs : GenericArmourItem(
	material = GenericArmourMaterial(
		"bugmagicdev",
		"${Reference.MOD_ID}:bugmagicdev"
	),
	equipmentSlot = EntityEquipmentSlot.FEET
) {
	override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
		if (this.isInCreativeTab(tab)) {
			items.add(ItemStack(this).apply {
				addEnchantment(
					Enchantment.REGISTRY
						.getObject(ResourceLocation(
							"binding_curse"
						))!!,
					1
				)
				addEnchantment(
					Enchantment.REGISTRY.getObject(ResourceLocation(
						"blast_protection"
					))!!,
					4
				)
			})
		}
	}

	override fun hasOverlay(stack: ItemStack): Boolean = true

	// Copied from Minecraft
	override fun hasColor(stack: ItemStack): Boolean {
		val nbttagcompound = stack.tagCompound

		return if (nbttagcompound != null && nbttagcompound.hasKey(
				"display",
				10
			)
		) nbttagcompound.getCompoundTag("display").hasKey("color", 3) else false
	}

	override fun getColor(stack: ItemStack): Int {
		val nbttagcompound = stack.tagCompound

		if (nbttagcompound != null) {
			val nbttagcompound1 = nbttagcompound.getCompoundTag("display")

			if (nbttagcompound1.hasKey("color", 3)) {
				return nbttagcompound1.getInteger("color")
			}
		}

		return EnumDyeColor.PINK.colorValue
	}

	override fun setColor(stack: ItemStack, color: Int) {
		var nbttagcompound = stack.tagCompound

		if (nbttagcompound == null) {
			nbttagcompound = NBTTagCompound()
			stack.tagCompound = nbttagcompound
		}

		val nbttagcompound1 = nbttagcompound.getCompoundTag("display")

		if (!nbttagcompound.hasKey("display", 10)) {
			nbttagcompound.setTag("display", nbttagcompound1)
		}

		nbttagcompound1.setInteger("color", color)
	}
}
