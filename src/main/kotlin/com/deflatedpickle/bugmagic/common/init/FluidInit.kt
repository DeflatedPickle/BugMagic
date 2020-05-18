/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry

object FluidInit {
    val BUG_ESSENCE = register("bug_essence")

    fun register(name: String): Fluid {
        val fluid = Fluid(
                name,
                ResourceLocation(Reference.MOD_ID, "${name}_still"),
                ResourceLocation(Reference.MOD_ID, "${name}_flowing")
        )

        FluidRegistry.registerFluid(fluid)
        FluidRegistry.addBucketForFluid(fluid)

        return fluid
    }
}
