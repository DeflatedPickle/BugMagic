package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.items.ItemBugNet
import com.deflatedpickle.bugmagic.items.ItemWand

object ModItems {
    val wandGeneric = ItemWand("wand_generic", 1, ModCreativeTabs.tabGeneral)

    val bugNet = ItemBugNet("bug_net", 1, ModCreativeTabs.tabGeneral, 5)
}