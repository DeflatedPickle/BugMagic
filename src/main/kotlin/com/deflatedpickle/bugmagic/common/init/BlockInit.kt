/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.block.BugBundle
import com.deflatedpickle.bugmagic.common.block.SpellTable
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraftforge.fluids.BlockFluidFinite

@AutoRegistry(Reference.MOD_ID)
object BlockInit {
    @JvmField
    val BUG_BUNDLE = BugBundle()

    @JvmField
    val SPELL_TABLE = SpellTable()

    @JvmField
    val BUG_ESSENCE = BlockFluidFinite(FluidInit.BUG_ESSENCE, MaterialInit.BUG_ESSENCE)
}
