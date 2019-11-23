package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.item.Generic
import com.deflatedpickle.bugmagic.common.item.Wand
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraft.creativetab.CreativeTabs

@AutoRegistry(Reference.MOD_ID)
object Item {
    // TODO: Different primitive bug types, like one you can only catch on the ground or on trees
    @JvmField
    val BUG = Generic("bug", CreativeTabs.MISC)

    @JvmField
    val BASIC_WAND = Wand("basic_wand")
}