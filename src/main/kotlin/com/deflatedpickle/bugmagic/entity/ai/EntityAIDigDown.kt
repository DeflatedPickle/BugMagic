package com.deflatedpickle.bugmagic.entity.ai

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class EntityAIDigDown(private val entityIn: EntityTameable) : EntityAIBase() {
    lateinit var owner: EntityLivingBase
    private var isBreaking = false
    private var blockTime = 0f

    override fun shouldExecute(): Boolean {
        val entityLivingBase = this.entityIn.owner

        return if (entityLivingBase != null) {
            owner = entityLivingBase
            true
        }
        else {
            false
        }
    }

    override fun updateTask() {
        if (!entityIn.world.isRemote) {
            val pos = BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)
            val blockState = entityIn.world.getBlockState(pos)
            val block = blockState.block

            if (block != Blocks.BEDROCK) {
                if (!isBreaking) {
                    // blockTime = ForgeEventFactory.getBreakSpeed(owner as EntityPlayer, blockState, 1f, pos)
                    // blockTime = (owner as EntityPlayer).getDigSpeed(blockState, pos)
                    blockTime = ItemStack(Items.DIAMOND_PICKAXE).getDestroySpeed(blockState) * 20
                    isBreaking = true
                }
                else {
                    blockTime--
                }

                if (blockTime <= 0) {
                    if (owner is EntityPlayer) {
                        (owner as EntityPlayer).inventory.addItemStackToInventory(ItemStack(block))
                    }

                    entityIn.world.destroyBlock(pos, false)
                    isBreaking = false
                    // block.breakBlock(entityIn.world, pos, blockState)
                }
            }
            else {
                entityIn.setDead()
            }
        }
    }
}