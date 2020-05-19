/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * A spell that summons [EssenceCollectorEntity]
 *
 * @author DeflatedPickle
 */
class EssenceCollectorSpell : Spell() {
    override fun getName(): String = "Essence Collector"
    override fun getManaLoss(): Int = 40
    override fun getCastCount(): Int = 2
    override fun getMaxCount(): Int = 2
    override fun getTier(): Tier = Tier.RARE
    override fun getCult(): Cult = Cult.APOIDEA

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(EssenceCollectorEntity::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(EssenceCollectorEntity::class.java, entityPlayer, itemWand)
    }
}
