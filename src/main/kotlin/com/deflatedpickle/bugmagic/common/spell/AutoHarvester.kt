/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvester as AutoHarvesterMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoe as AutoHoeMob

// Note: This bug is based on Thrips
class AutoHarvester : ASpell() {
    override fun getName(): String = "Auto Harvester"
    override fun getManaLoss(): Int = 45
    override fun getCastCount(): Int = 2
    override fun getMaxCount(): Int = 6
    override fun getTier(): Tier = Tier.UNCOMMON
    override fun getCult(): Cult = Cult.INSECT

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoHarvesterMob::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoHarvesterMob::class.java, entityPlayer, itemWand)
    }
}
