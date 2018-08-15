package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack
import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import com.deflatedpickle.bugmagic.entity.mob.EntityWurm
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object ModEntities {
    init {
        EntityRegistry.registerModEntity(ResourceLocation(Reference.MOD_ID + ":firefly"), EntityFirefly::class.java, "firefly", 0, BugMagic, 16, 1, false, 0x40362A, 0xFFEB66)
        EntityRegistry.registerModEntity(ResourceLocation(Reference.MOD_ID + ":bugpack"), EntityBugpack::class.java, "bugpack", 1, BugMagic, 16, 1, false, 0x7F3918, 0x7F5562)
        EntityRegistry.registerModEntity(ResourceLocation(Reference.MOD_ID + ":wurm"), EntityWurm::class.java, "wurm", 2, BugMagic, 16, 1, false, 0x40273B, 0xBF76B0)
    }
}