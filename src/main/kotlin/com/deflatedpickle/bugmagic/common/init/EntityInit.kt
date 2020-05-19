/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvesterEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoeEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanterEntity
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorEntity
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import kotlin.reflect.KClass
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object EntityInit {
    var id = 0

    init {
        register("item_collector", ItemCollectorEntity::class, Pair(0x40362A, 0xFFEB66))
        register("essence_collector", EssenceCollectorEntity::class, Pair(0x40362A, 0xFFEB66))
        register("auto_hoe", AutoHoeEntity::class, Pair(0x40362A, 0xFFEB66))
        register("auto_planter", AutoPlanterEntity::class, Pair(0x40362A, 0xFFEB66))
        register("auto_fertilizer", AutoFertilizerEntity::class, Pair(0x40362A, 0xFFEB66))
        register("auto_harvester", AutoHarvesterEntity::class, Pair(0x40362A, 0xFFEB66))
    }

    fun register(name: String, clazz: KClass<out EntityCastable>, eggColour: Pair<Int, Int>) {
        EntityRegistry.registerModEntity(
                ResourceLocation(Reference.MOD_ID + ":$name"),
                clazz.java, name, id++, BugMagic, 16, 1, false,
                eggColour.first, eggColour.second
        )
    }
}
