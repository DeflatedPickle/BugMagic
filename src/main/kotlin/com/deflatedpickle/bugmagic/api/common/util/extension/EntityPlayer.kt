package com.deflatedpickle.bugmagic.api.common.util.extension

import com.deflatedpickle.bugmagic.common.init.ItemInit
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot

fun EntityPlayer.isWearingDevArmour() =
	this.getItemStackFromSlot(EntityEquipmentSlot.FEET).item == ItemInit.THIGH_HIGHS
