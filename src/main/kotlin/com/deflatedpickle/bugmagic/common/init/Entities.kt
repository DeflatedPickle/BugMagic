package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object Entities {
    init {
        EntityRegistry.registerModEntity(ResourceLocation(Reference.MOD_ID + ":item_collector"), ItemCollector::class.java, "item_collector", 0, BugMagic, 16, 1, false, 0x40362A, 0xFFEB66)
    }
}