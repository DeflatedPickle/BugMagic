/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvesterEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [AutoHarvesterEntity]
 *
 * @author DeflatedPickle
 */
// Note: This bug is based on Thrips
class AutoHarvesterSpell : Spell("Auto Harvester") {
    init {
        this.manaLoss = 45
        this.castCount = 2
        this.maxCount = 6
        this.tier = Tier.UNCOMMON
        this.cult = Cult.INSECT
    }

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoHarvesterEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoHarvesterEntity::class.java, entityPlayer, itemWand)
    }
}
