package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly

class SpellFirefly : SpellBase() {
    init {
        name = "Firefly"
        cost = 15
        drain = 2
        drainWait = 60
    }

    override fun cast() {
        val entity = EntityFirefly(caster!!.world)
        entity.ownerId = caster!!.gameProfile.id
        entity.setPositionAndRotation(caster!!.posX + 1, caster!!.posY, caster!!.posZ + 1, 0f, 0f)
        // entity.startRiding(caster)

        caster!!.world.spawnEntity(entity)
    }
}