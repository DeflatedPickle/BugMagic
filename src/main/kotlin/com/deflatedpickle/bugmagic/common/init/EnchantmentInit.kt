package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.enchantment.EnchantmentBloodleak
import com.github.upcraftlp.glasspane.api.registry.AutoRegistry

@AutoRegistry(Reference.MOD_ID)
object EnchantmentInit {
	@JvmField
	val BLOOD_LEAK = EnchantmentBloodleak()
}
