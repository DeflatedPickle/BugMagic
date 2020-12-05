/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.client.util.extension

import java.util.concurrent.ThreadLocalRandom
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumParticleTypes

fun EnumParticleTypes.spawn(
    entityLivingBase: EntityLivingBase,
    radius: Double = 0.0,
    plusX: Double = 0.0,
    plusY: Double = 0.0,
    plusZ: Double = 0.0
) {
    var randomX = 0.0
    var randomY = 0.0
    var randomZ = 0.0

    // We don't need to generate random numbers for 0.0
    if (radius > 0.0) {
        randomX = ThreadLocalRandom.current().nextDouble(-radius, radius)
        randomY = ThreadLocalRandom.current().nextDouble(-radius, radius)
        randomZ = ThreadLocalRandom.current().nextDouble(-radius, radius)
    }

    entityLivingBase.world.spawnParticle(
        // Our particle
        this,
        // The position on each axis
        entityLivingBase.posX + plusX + randomX,
        entityLivingBase.posY + plusY + randomY,
        entityLivingBase.posZ + plusZ + randomZ,
        0.0, 0.0, 0.0
    )
}
