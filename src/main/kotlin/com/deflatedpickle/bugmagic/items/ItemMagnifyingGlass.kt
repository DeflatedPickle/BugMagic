package com.deflatedpickle.bugmagic.items

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ItemMagnifyingGlass : Item() {
    init {
        setMaxStackSize(1)
        maxDamage = 64
    }

    // The use is implemented in BlockBugJar, maybe move parts here?

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return true
    }
}