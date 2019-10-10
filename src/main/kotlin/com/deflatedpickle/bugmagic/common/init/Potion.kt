package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.potion.BugEssenceRegeneration
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry

@AutoRegistry(Reference.MOD_ID)
object Potion {
    @JvmField
    val BUG_ESSENCE_REGENERATION = BugEssenceRegeneration()
}