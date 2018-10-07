package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.picklelib.tabs.TabBase
import ladylib.LadyLib
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

object ModCreativeTabs {
    val tabGeneral = TabBase("general", ItemStack(Items.STICK), false)

    init {
        LadyLib.INSTANCE.getContainer(Reference.MOD_ID).creativeTab = tabGeneral
    }
}