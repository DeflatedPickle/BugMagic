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
class AutoHarvesterSpell : Spell() {
    override fun getName(): String = "Auto Harvester"
    override fun getManaLoss(): Int = 45
    override fun getCastCount(): Int = 2
    override fun getMaxCount(): Int = 6
    override fun getTier(): Tier = Tier.UNCOMMON
    override fun getCult(): Cult = Cult.INSECT

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoHarvesterEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoHarvesterEntity::class.java, entityPlayer, itemWand)
    }
}