package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.item.Generic
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraft.creativetab.CreativeTabs

@AutoRegistry(Reference.MOD_ID)
object Item {
    @JvmField
    val BUG = Generic("bug", CreativeTabs.MISC)
}