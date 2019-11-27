package com.deflatedpickle.bugmagic.common.entity.mob

import com.deflatedpickle.bugmagic.api.entity.mob.EntityCastable
import com.deflatedpickle.bugmagic.common.entity.ai.*
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler

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
        this.tasks.addTask(1, FindInventory(this))
        this.tasks.addTask(1, CollectItem(this))
        this.tasks.addTask(2, WalkToBlock(this))
        this.tasks.addTask(3, DeliverToInventory(this))
        this.tasks.addTask(4, WalkToItem(this))
    }

    override fun processInteract(player: EntityPlayer, hand: EnumHand): Boolean {
        if (player.getHeldItem(hand).item is Wand) {
            this.dataManager.set(dataInventoryPosition, NBTUtil.getPosFromTag(player.getHeldItem(hand).tagCompound!!))
            return true
        }
        return false
    }
}