package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.block.Generic
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

@AutoRegistry(Reference.MOD_ID)
object Block {
    @JvmField
    val BUG_BUNDLE = Generic("bug_bundle", CreativeTabs.DECORATIONS, Material.CLOTH)
}