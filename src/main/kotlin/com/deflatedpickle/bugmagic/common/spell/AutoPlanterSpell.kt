/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanterEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [AutoPlanterEntity]
 *
 * @author DeflatedPickle
 */
class AutoPlanterSpell : Spell("Auto Planter") {
    init {
        this.manaLoss = 24
        this.castCount = 1
        this.maxCount = 2
        this.tier = Tier.COMMON
        this.cult = Cult.INSECT
    }

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoPlanterEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoPlanterEntity::class.java, entityPlayer, itemWand)
    }
}
