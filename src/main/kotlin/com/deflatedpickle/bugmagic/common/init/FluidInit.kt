/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.common.fluid.GenericFluid
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry

object FluidInit {
    val BUG_ESSENCE = register("bug_essence")

    fun register(name: String): Fluid {
        val fluid = GenericFluid(
            name,
            ResourceLocation(Reference.MOD_ID, "blocks/${name}_still"),
            ResourceLocation(Reference.MOD_ID, "blocks/${name}_flow"),
            ResourceLocation(Reference.MOD_ID, "blocks/${name}_overlay")
        )

        FluidRegistry.registerFluid(fluid)
        FluidRegistry.addBucketForFluid(fluid)

        return fluid
    }
}
