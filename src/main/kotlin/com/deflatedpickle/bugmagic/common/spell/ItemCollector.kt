package com.deflatedpickle.bugmagic.common.spell

import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector as ItemCollectorMob
import net.minecraft.entity.player.EntityPlayer

class ItemCollector : ASpell() {
    init {
        this.setRegistryName("spell_item_collector")
    }

    override fun getName(): String = "Item Collector"
    override fun getManaCost(): Int = 22
    override fun getTier(): Tier = Tier.COMMON

    override fun cast(entityPlayer: EntityPlayer) {
        val entity = ItemCollectorMob(entityPlayer.world)
        entity.ownerId = entityPlayer.uniqueID
        entity.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)

        entityPlayer.world.spawnEntity(entity)
    }

    override fun uncast(entityPlayer: EntityPlayer) {
    }
}