package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.common.init.ItemInit
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumDyeColor
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ColourHandlerEvent {
	@SubscribeEvent
	fun registerColorHandlers(event: ColorHandlerEvent.Item) {
		event.itemColors.registerItemColorHandler(IItemColor { stack, tintIndex ->
			val nbtTagCompound = stack.tagCompound!!

			return@IItemColor if (tintIndex == 1) {
				if (
					nbtTagCompound.hasKey("display", 10) &&
					nbtTagCompound.getCompoundTag("display").hasKey("color", 3)
				) {
					nbtTagCompound
						.getCompoundTag("display")
						.getInteger("color")
				} else {
					EnumDyeColor.PINK.colorValue
				}
			} else -1
		}, ItemInit.THIGH_HIGHS)
	}
}
