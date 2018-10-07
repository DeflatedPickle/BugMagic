package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack

class SpellBugpack : SpellBase() {
    lateinit var entity: EntityBugpack

    init {
        name = "Bugpack"
        id = 2
        cost = 15
        drain = 4
        drainWait = 1200
        castLimit = 3
        cooldownTime = 25

        addToMap()
    }

    override fun limitedCast() {
        entity = EntityBugpack(caster.world)
        entity.ownerId = caster.gameProfile.id
        entity.setPositionAndRotation(caster.posX + 1, caster.posY, caster.posZ + 1, 0f, 0f)

        caster.world.spawnEntity(entity)
    }

    override fun uncast() {
        super.uncast()

        entity.world.removeEntityDangerously(entity)
        entity.world.removeEntity(entity)
    }
}