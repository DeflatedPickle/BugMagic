package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.item.ItemStack

class ItemMagnifyingGlass(name: String) : ItemBase(name, 1, ModCreativeTabs.tabGeneral) {
    init {
        maxDamage = 64
    }

    // The use is implemented in BlockBugJar, maybe move parts here?

    override fun showDurabilityBar(stack: ItemStack?): Boolean {
        return true
    }
}