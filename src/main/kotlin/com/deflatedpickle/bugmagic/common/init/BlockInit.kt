/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.block.CrownOfTheLordTotemBlock
import com.deflatedpickle.bugmagic.common.block.BugBundleBlock
import com.deflatedpickle.bugmagic.common.block.SpellTableBlock
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraftforge.fluids.BlockFluidFinite

@AutoRegistry(Reference.MOD_ID)
object BlockInit {
    @JvmField
    val BUG_BUNDLE = BugBundleBlock()

    @JvmField
    val SPELL_TABLE = SpellTableBlock()

	// Totems

	@JvmField
	val PIG_STICK = CrownOfTheLordTotemBlock()

	// Fluid

    @JvmField
    val BUG_ESSENCE = BlockFluidFinite(FluidInit.BUG_ESSENCE, MaterialInit.BUG_ESSENCE)
}
