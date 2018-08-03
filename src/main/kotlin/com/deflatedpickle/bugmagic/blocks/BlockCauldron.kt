package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.items.ItemBugNet
import com.deflatedpickle.bugmagic.items.ItemBugPart
import com.deflatedpickle.bugmagic.tileentity.TileEntityBugJar
import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.picklelib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.commons.lang3.tuple.ImmutablePair

class BlockCauldron(name: String, val stirsRequired: Int) : BlockBase(name, Material.IRON, 2f, 10f, ImmutablePair("pickaxe", 0), ModCreativeTabs.tabGeneral), ITileEntityProvider {
    var fullyStirred = false

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn!!.getTileEntity(pos!!)

        if (worldIn.isRemote) {
            if (tileEntity is TileEntityCauldron) {
                if (hand == EnumHand.MAIN_HAND) {
                    val itemStack = playerIn!!.getHeldItem(hand)

                    if (itemStack.isEmpty) {
                        // TODO: Stir the cauldron, if it has a stirring stick
                        if (tileEntity.hasStirrer) {
                            if (tileEntity.waterAmount == 1f) {
                                tileEntity.stirAmount++
                            }

                            if (tileEntity.getParts() > 0) {
                                if (tileEntity.stirAmount >= stirsRequired / (tileEntity.getParts() / 2)) {
                                    fullyStirred = true
                                }
                            }
                        }
                    }
                    else {
                        when (itemStack.item) {
                            Items.STICK -> {
                                // At a stirring stick
                                if (!tileEntity.hasStirrer) {
                                    tileEntity.hasStirrer = true
                                    itemStack.shrink(1)
                                }
                            }
                            Items.WATER_BUCKET -> {
                                // Fill the cauldron
                                tileEntity.waterAmount = tileEntity.maxWater
                                itemStack.shrink(1)
                                playerIn.inventory.addItemStackToInventory(ItemStack(Items.BUCKET))
                            }
                            Items.BUCKET -> {
                                // Empty the cauldron
                                if (tileEntity.waterAmount == tileEntity.maxWater) {
                                    itemStack.shrink(1)

                                    if (fullyStirred) {
                                        // TODO: Create a fluid for bug resonance
                                    }
                                    else {
                                        playerIn.inventory.addItemStackToInventory(ItemStack(Items.WATER_BUCKET))
                                    }
                                }
                            }
                            is ItemBugPart -> {
                                // Add a part to the mix
                                if (tileEntity.getParts() < tileEntity.maxParts) {
                                    tileEntity.addParts(1)
                                    itemStack.shrink(1)
                                }
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TileEntityCauldron(8)
    }
}