/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.item.Wand
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry

@Suppress("unused")
@AutoRegistry(Reference.MOD_ID)
object ItemInit {
    @JvmField
    val BASIC_WAND = Wand("basic_wand", 32)
}
