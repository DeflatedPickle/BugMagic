package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

open class CommonProxy {
    fun preInit(event: FMLPreInitializationEvent) {
        ModCreativeTabs
        ModItems
    }
}