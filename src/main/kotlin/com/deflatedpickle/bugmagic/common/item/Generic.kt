package com.deflatedpickle.bugmagic.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

class Generic(name: String, creativeTab: CreativeTabs) : Item() {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab
    }
}