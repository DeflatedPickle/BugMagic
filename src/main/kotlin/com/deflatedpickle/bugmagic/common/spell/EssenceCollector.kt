package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.WorldServer
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollector as EssenceCollectorMob

class EssenceCollector : ASpell() {
    override fun getName(): String = "Essence Collector"
    override fun getManaLoss(): Int = 40
    override fun getCastCount(): Int = 2
    override fun getMaxCount(): Int = 2
    override fun getTier(): Tier = Tier.RARE

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(EssenceCollectorMob::class.java, entityPlayer, itemWand)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(itemWand, entityPlayer.world as WorldServer)
    }
}