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
    }

    override fun initEntityAI() {
        /*
            AI Steps:
            1. Finds an inventory
            2. Collects an item (if near one)
            3. Walks to the inventory (if it found one)
            4. Delivers the item to the inventory (if it found one)
            5. Looks for an item
            - Repeats steps 3 through 5
         */

        val findItem = AIFindItem(this, { this.dataManager.get(dataHomePosition) }, 7.0)

        this.tasks.addTask(1, AIFindClosestTileEntity(this, dataHomePosition, {
            it is IInventory
        }, {
            if (it != null) {
                this.dataManager.set(dataHomePosition, it.pos)
            }
        }))
        this.tasks.addTask(1, AICollectItem(findItem, this))
        this.tasks.addTask(2, AIWalkToBlockPos(this, { !this.dataManager.get(dataItemStack).isEmpty }, { this.dataManager.get(dataHomePosition) }))
        this.tasks.addTask(3, AIDeliverToInventory(
            findItem,
            this,
            dataItemStack,
            dataHomePosition)
        )
        this.tasks.addTask(4, findItem)
        this.tasks.addTask(4, AIWalkToItem(findItem, this))
    }

    override fun processInteract(player: EntityPlayer, hand: EnumHand): Boolean {
        if (player.getHeldItem(hand).item is Wand) {
            this.dataManager.set(dataHomePosition, NBTUtil.getPosFromTag(player.getHeldItem(hand).tagCompound!!))
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
