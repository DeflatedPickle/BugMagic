package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.common.block.Generic
import com.deflatedpickle.bugmagic.common.item.Wand
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemBucket
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.ForgeModContainer
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidActionResult
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.UniversalBucket
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import java.util.*

class SpellTable : Generic("spell_table", CreativeTabs.DECORATIONS, Material.WOOD, lightOpacity = 0, isFullBlock = false, isOpaqueCube = false, renderLayer = BlockRenderLayer.CUTOUT) {
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
                            // Fill with a fluid
                            when (stack.item) {
                                // Add a wand
                                is Wand -> ItemHandlerHelper.insertItemStacked(tileEntity.wandStackHandler, stack, false)
                                // Add any other item to the general inventory
                                else -> {
                                    if (!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                                        ItemHandlerHelper.insertItemStacked(tileEntity.itemStackHandler, stack.splitStack(1), false)
                                    }
                                    else {
                                        var result = FluidUtil.tryEmptyContainer(stack, tileEntity.fluidTank, Fluid.BUCKET_VOLUME, playerIn, true)

                                        if (!result.success) {
                                            result = FluidUtil.tryFillContainer(stack, tileEntity.fluidTank, Fluid.BUCKET_VOLUME, playerIn, true)
                                        }

                                        if (result.isSuccess) {
                                            playerIn.setHeldItem(hand, result.getResult())
                                        }
                                    }
                                }
                            }

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