package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.item.Wand
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector as ItemCollectorMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.WorldServer

class ItemCollector : ASpell() {
    override fun getName(): String = "Item Collector"
    override fun getManaLoss(): Int = 22
    override fun getMaxCount(): Int = 3
    override fun getTier(): Tier = Tier.COMMON

    override fun cast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.summonEntity(ItemCollectorMob::class.java, entityPlayer, itemWand, 1)
    }

    override fun uncast(entityPlayer: EntityPlayer, itemWand: ItemStack) {
        this.killAllEntities(itemWand, entityPlayer.world as WorldServer)
    }
}