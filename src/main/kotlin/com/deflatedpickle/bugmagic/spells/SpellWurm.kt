package com.deflatedpickle.bugmagic.spells

import com.deflatedpickle.bugmagic.entity.mob.EntityWurm

class SpellWurm : SpellBase() {
    var entity: EntityWurm? = null

    init {
        name = "Wurm"
        id = 3
        cost = 25
        drain = 1
        drainWait = 800
        castLimit = 4
        cooldownTime = 30

        addToMap()
    }

    override fun limitedCast() {
        entity = EntityWurm(caster!!.world)
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