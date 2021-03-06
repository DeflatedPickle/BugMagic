/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.potion.BugEssenceRegenerationPotionEffect
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry

@AutoRegistry(Reference.MOD_ID)
object PotionInit {
    @JvmField
    val BUG_ESSENCE_REGENERATION = BugEssenceRegenerationPotionEffect()
}
