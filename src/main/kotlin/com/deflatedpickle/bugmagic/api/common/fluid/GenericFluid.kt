package com.deflatedpickle.bugmagic.api.common.fluid

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid

class GenericFluid(
	name: String,
	stillTexturePath: ResourceLocation,
	flowingTexturePath: ResourceLocation,
	overlayTexturePath: ResourceLocation?,
	density: Int = 1000,
	viscosity: Int = 1000,
	temperature: Int = 300,
	luminosity: Int = 0,
	gaseous: Boolean = false
) : Fluid(
	name,
	stillTexturePath,
	flowingTexturePath,
	overlayTexturePath
) {
	init {
		this.density = density
		this.viscosity = viscosity
		this.temperature = temperature
		this.luminosity = luminosity

		this.isGaseous = gaseous
	}
}
