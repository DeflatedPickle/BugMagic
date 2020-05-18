/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizer
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvester
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoe
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanter
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorMob
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import kotlin.reflect.KClass
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object EntityInit {
    var id = 0

    init {
        register("item_collector", ItemCollector::class, Pair(0x40362A, 0xFFEB66))
        register("essence_collector", EssenceCollectorMob::class, Pair(0x40362A, 0xFFEB66))
        register("auto_hoe", AutoHoe::class, Pair(0x40362A, 0xFFEB66))
        register("auto_planter", AutoPlanter::class, Pair(0x40362A, 0xFFEB66))
        register("auto_fertilizer", AutoFertilizer::class, Pair(0x40362A, 0xFFEB66))
        register("auto_harvester", AutoHarvester::class, Pair(0x40362A, 0xFFEB66))
    }

    fun register(name: String, clazz: KClass<out EntityCastable>, eggColour: Pair<Int, Int>) {
        EntityRegistry.registerModEntity(
                ResourceLocation(Reference.MOD_ID + ":$name"),
                clazz.java, name, id++, BugMagic, 16, 1, false,
                eggColour.first, eggColour.second
        )
    }
}
