/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class WalkToItem(private val findItem: FindItem, private val entityIn: EntityLiving) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        if (entityIn is EntityTameable) {
            if (entityIn.owner == null) {
                return false
            }
        }

        val stack = entityIn.dataManager.get(ItemCollector.dataItemStack)
        if ((stack.isEmpty || stack == ItemStack.EMPTY || stack.item == Items.AIR || stack.item == Blocks.AIR) &&
                entityIn.dataManager.get(ItemCollector.dataInventoryPosition) != BlockPos.ORIGIN &&
                entityIn.world.getTileEntity(entityIn.dataManager.get(ItemCollector.dataInventoryPosition)) != null) {
            return true
        }
        return false
    }

    override fun updateTask() {
        var item: EntityItem? = null
        findItem.entity?.let {
            item = it

            val path = this.entityIn.navigator.getPathToEntityLiving(it)

            path?.let { oldTownRoad ->
                this.entityIn.navigator.setPath(oldTownRoad, entityIn.aiMoveSpeed.toDouble())
            }
        }
    }
}
