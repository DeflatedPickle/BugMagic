package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.common.block.Generic
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import java.util.*

class SpellTable : Generic("spell_table", CreativeTabs.DECORATIONS, Material.WOOD) {
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote) {
            val tileEntity = worldIn.getTileEntity(pos)

            if (tileEntity is SpellTableTE) {
                val itemCount = 0.until(tileEntity.itemStackHandler.slots).count { tileEntity.itemStackHandler.getStackInSlot(it) != ItemStack.EMPTY }
                val stack = playerIn.getHeldItem(hand)

                if (playerIn.canPlayerEdit(pos, facing, stack)) {
                    if (playerIn.isSneaking) {
                        if (itemCount - 1 >= 0) {
                            val dropStack = tileEntity.itemStackHandler.extractItem(itemCount - 1, 1, false)
                            val entity = EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble() + 1, pos.z.toDouble(), dropStack)
                            worldIn.spawnEntity(entity)

                            worldIn.markBlockRangeForRenderUpdate(pos, pos)
                            worldIn.notifyBlockUpdate(pos, state, state, 3)
                            worldIn.scheduleBlockUpdate(pos, this, 0, 0)
                            tileEntity.markDirty()
                        }
                    }
                    else {
                        if (stack != ItemStack.EMPTY) {
                            ItemHandlerHelper.insertItemStacked(tileEntity.itemStackHandler, stack.splitStack(1), false)

                            worldIn.markBlockRangeForRenderUpdate(pos, pos)
                            worldIn.notifyBlockUpdate(pos, state, state, 3)
                            worldIn.scheduleBlockUpdate(pos, this, 0, 0)
                            tileEntity.markDirty()
                        }
                    }
                }
            }
        }

        return true
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val tileEntity = worldIn.getTileEntity(pos)

        if (tileEntity is SpellTableTE) {
            for (i in 0 until tileEntity.itemStackHandler.slots) {
                val stack = tileEntity.itemStackHandler.getStackInSlot(i)

                if (stack != ItemStack.EMPTY) {
                    val entity = EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble() + 1, pos.z.toDouble(), stack)
                    worldIn.spawnEntity(entity)
                }
            }
        }

        super.breakBlock(worldIn, pos, state)
    }

    override fun hasTileEntity(state: IBlockState): Boolean = true
    override fun createTileEntity(world: World, state: IBlockState): TileEntity? = SpellTableTE()
}