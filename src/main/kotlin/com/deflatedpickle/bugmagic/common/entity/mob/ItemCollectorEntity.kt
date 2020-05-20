/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.AICollectItem
import com.deflatedpickle.bugmagic.common.entity.ai.AIDeliverToInventory
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindClosestTileEntity
import com.deflatedpickle.bugmagic.common.entity.ai.AIFindItem
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToBlockPos
import com.deflatedpickle.bugmagic.common.entity.ai.AIWalkToItem
import com.deflatedpickle.bugmagic.common.item.Wand
import com.deflatedpickle.bugmagic.common.spell.ItemCollectorSpell
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

/**
 * The entity summoned with [ItemCollectorSpell]
 *
 * @author DeflatedPickle
 * @param worldIn The world this entity belongs to
 */
class ItemCollectorEntity(worldIn: World) : EntityCastable(worldIn) {
    companion object {
        val dataItemStack: DataParameter<ItemStack> = EntityDataManager.createKey(ItemCollectorEntity::class.java, DataSerializers.ITEM_STACK)
        val dataInventoryPosition: DataParameter<BlockPos> = EntityDataManager.createKey(ItemCollectorEntity::class.java, DataSerializers.BLOCK_POS)
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
        /*
            AI Steps:
            - Finds an inventory
            - Collects an item (if near one)
            - Walks to the inventory (if it found one)
            - Delivers the item to the inventory (if it found one)
            - Looks for an item
            - Walks to an item (if it found one)
         */

        val findItem = AIFindItem(this, { this.dataManager.get(dataInventoryPosition) }, 7.0)

        this.tasks.addTask(1, AIFindClosestTileEntity(this, dataInventoryPosition, {
            it is IInventory
        }, {
            if (it != null) {
                this.dataManager.set(dataInventoryPosition, it.pos)
            }
        }))
        this.tasks.addTask(1, AICollectItem(findItem, this))
        this.tasks.addTask(2, AIWalkToBlockPos(this, { !this.dataManager.get(dataItemStack).isEmpty }, { this.dataManager.get(dataInventoryPosition) }))
        this.tasks.addTask(3, AIDeliverToInventory(findItem, this))
        this.tasks.addTask(4, findItem)
        this.tasks.addTask(4, AIWalkToItem(findItem, this))
    }

    override fun processInteract(player: EntityPlayer, hand: EnumHand): Boolean {
        if (player.getHeldItem(hand).item is Wand) {
            this.dataManager.set(dataInventoryPosition, NBTUtil.getPosFromTag(player.getHeldItem(hand).tagCompound!!))
            return true
        } else {
            val itemStack = this.dataManager.get(dataItemStack)
            if (itemStack != ItemStack.EMPTY) {
                player.addItemStackToInventory(itemStack)
                itemStack.shrink(1)
            }
        }
        return false
    }

    override fun getAIMoveSpeed(): Float {
        return 0.2f
    }
}
