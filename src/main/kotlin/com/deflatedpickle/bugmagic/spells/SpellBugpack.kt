package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.entity.mob.EntityBugpack

class SpellBugpack : SpellBase() {
    init {
        name = "Bugpack"
        id = 2
        cost = 15
        drain = 4
        drainWait = 60

        addToMap()
    }

    override fun cast() {
        val entity = EntityBugpack(caster!!.world)
        entity.ownerId = caster!!.gameProfile.id
        entity.setPositionAndRotation(caster!!.posX + 1, caster!!.posY, caster!!.posZ + 1, 0f, 0f)

        caster!!.world.spawnEntity(entity)
    }
}