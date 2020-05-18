/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

open class Generic(name: String, creativeTab: CreativeTabs) : Item() {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab
    }
}
