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
class AutoHoeSpell : Spell() {
    override fun getName(): String = "Auto Hoe"
    override fun getManaLoss(): Int = 24
    override fun getCastCount(): Int = 1
    override fun getMaxCount(): Int = 2
    override fun getTier(): Tier = Tier.COMMON
    override fun getCult(): Cult = Cult.GASTROPOD

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoHoeEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoHoeEntity::class.java, entityPlayer, itemWand)
    }
}
