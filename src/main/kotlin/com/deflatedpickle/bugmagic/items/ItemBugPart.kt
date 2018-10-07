package com.deflatedpickle.bugmagic.items

import net.minecraft.item.Item

class ItemBugPart(val partType: String) : Item() {
    init {
        setMaxStackSize(16)
    }
    // TODO: Change the partType to an Enum
}