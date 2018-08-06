package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly

class SpellFirefly : SpellBase() {
    var entity: EntityFirefly? = null

    init {
        name = "Firefly"
        id = 1
        cost = 15
        drain = 2
        drainWait = 60
        castLimit = 1
        cooldownTime = 20

        addToMap()
    }

    override fun limitedCast() {
        entity = EntityFirefly(caster!!.world)
        entity!!.ownerId = caster!!.gameProfile.id
        entity!!.setPositionAndRotation(caster!!.posX + 1, caster!!.posY, caster!!.posZ + 1, 0f, 0f)
        // entity.startRiding(caster)

        caster!!.world.spawnEntity(entity!!)
    }

    override fun uncast() {
        super.uncast()

        entity!!.world.removeEntityDangerously(entity!!)
        entity!!.world.removeEntity(entity!!)
    }
}