/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.CollectItem
import com.deflatedpickle.bugmagic.common.entity.ai.DeliverToInventory
import com.deflatedpickle.bugmagic.common.entity.ai.FindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.FindItem
import com.deflatedpickle.bugmagic.common.entity.ai.WalkToItem
import com.deflatedpickle.bugmagic.common.entity.ai.WalkToTileEntity
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemCollector(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataItemStack: DataParameter<ItemStack> = EntityDataManager.createKey(ItemCollector::class.java, DataSerializers.ITEM_STACK)
        val dataInventoryPosition: DataParameter<BlockPos> = EntityDataManager.createKey(ItemCollector::class.java, DataSerializers.BLOCK_POS)
    }

    init {
        setSize(0.8f, 0.5f)
    }

    override fun getEyeHeight(): Float {
        return 0.14f
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataItemStack, ItemStack.EMPTY)
        this.dataManager.register(dataInventoryPosition, BlockPos.ORIGIN)
    }

    override fun initEntityAI() {
        val findItem = FindItem(this)

        this.tasks.addTask(1, FindClosestTileEntity(this, dataInventoryPosition, {
            it is IInventory
        }, {
            if (it != null) {
                this.dataManager.set(dataInventoryPosition, it.pos)
            }
        }))
        this.tasks.addTask(1, CollectItem(findItem, this))
        this.tasks.addTask(2, WalkToTileEntity(this, { !this.dataManager.get(dataItemStack).isEmpty }, dataInventoryPosition))
        this.tasks.addTask(3, DeliverToInventory(this))
        this.tasks.addTask(4, findItem)
        this.tasks.addTask(4, WalkToItem(findItem, this))
    }

    override fun processInteract(player: EntityPlayer, hand: EnumHand): Boolean {
        if (player.getHeldItem(hand).item is Wand) {
            this.dataManager.set(dataInventoryPosition, NBTUtil.getPosFromTag(player.getHeldItem(hand).tagCompound!!))
            return true
        }
        return false
    }

    override fun getAIMoveSpeed(): Float {
        return 0.2f
    }
}
