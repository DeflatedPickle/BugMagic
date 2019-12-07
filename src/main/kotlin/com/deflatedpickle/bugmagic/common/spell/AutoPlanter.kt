/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanter as AutoPlanterMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class AutoPlanter : ASpell() {
    override fun getName(): String = "Auto Planter"
    override fun getManaLoss(): Int = 24
    override fun getCastCount(): Int = 1
    override fun getMaxCount(): Int = 2
    override fun getTier(): Tier = Tier.COMMON
    override fun getCult(): Cult = Cult.INSECT

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(AutoPlanterMob::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(AutoPlanterMob::class.java, entityPlayer, itemWand)
    }
}
