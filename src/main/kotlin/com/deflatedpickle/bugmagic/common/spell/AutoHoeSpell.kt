/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoeEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [AutoHoeEntity]
 *
 * @author DeflatedPickle
 */
class AutoHoeSpell : Spell("Auto Hoe") {
    init {
        this.manaLoss = 24
        this.castCount = 1
        this.maxCount = 2
        this.tier = Tier.COMMON
        this.cult = Cult.GASTROPOD
    }

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoHoeEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoHoeEntity::class.java, entityPlayer, itemWand)
    }
}
