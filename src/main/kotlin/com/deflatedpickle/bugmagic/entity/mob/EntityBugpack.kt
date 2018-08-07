package com.deflatedpickle.bugmagic.entity.mob

import com.deflatedpickle.bugmagic.entity.ai.EntityAIHoverToOwner
import com.deflatedpickle.bugmagic.util.AltarUtil
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class EntityBugpack(worldIn: World) : EntityTameable(worldIn) {
    companion object {
        val dataItemStack: DataParameter<ItemStack> = EntityDataManager.createKey(EntityBugpack::class.java, DataSerializers.ITEM_STACK)
    }

    init {
        this.isImmuneToFire = true

        setSize(0.65f, 0.7f)

        isTamed = true

        enablePersistence()
    }

    override fun entityInit() {
        super.entityInit()

        this.dataManager.register(dataItemStack, ItemStack(Blocks.AIR))
    }

    override fun canBeLeashedTo(player: EntityPlayer?): Boolean {
        return false
    }

    override fun canDespawn(): Boolean {
        return false
    }

    override fun initEntityAI() {
        // this.tasks.addTask(2, EntityAIFollowOwner(this, 3.0, 0.5f, 50.0f))
        this.tasks.addTask(2, EntityAIHoverToOwner(this, 0.12f, -1.2f, 1.2f))
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 35.0
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.2
    }

    override fun createChild(ageable: EntityAgeable?): EntityAgeable? {
        return null
    }

    override fun processInteract(player: EntityPlayer?, hand: EnumHand?): Boolean {
        if (!player!!.world.isRemote) {
            if (player == owner) {
                if (hand == EnumHand.MAIN_HAND) {
                    val stack = player.getHeldItem(hand)

                    if (!stack.isEmpty && this.dataManager.get(dataItemStack).isEmpty) {
                        // Move the item into the bug
                        this.dataManager.set(dataItemStack, stack)
                        player.inventory.removeStackFromSlot(player.inventory.getSlotFor(stack))
                    }
                    else if (stack.isEmpty) {
                        // TODO: Spawn the item in the world instead
                        player.inventory.addItemStackToInventory(this.dataManager.get(dataItemStack))

                        this.dataManager.set(dataItemStack, ItemStack(Blocks.AIR))
                    }
                    // TODO: Add an option to swap a stack and merge a stack
                }
            }
        }

        return true
    }
}