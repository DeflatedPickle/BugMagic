package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.items.ItemBugPart
import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.picklelib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionUtils
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.commons.lang3.tuple.ImmutablePair

class BlockCauldron(name: String, private val stirsRequired: Int) : BlockBase(name, Material.IRON, 2f, 10f, ImmutablePair("pickaxe", 0), ModCreativeTabs.tabGeneral), ITileEntityProvider {
    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn.getTileEntity(pos)

        if (worldIn.isRemote) {
            if (tileEntity is TileEntityCauldron) {
                if (hand == EnumHand.MAIN_HAND) {
                    val itemStack = playerIn!!.getHeldItem(hand)

                    if (itemStack.isEmpty) {
                        // Stir the cauldron, if it has a stirring stick
                        if (tileEntity.hasStirrer) {
                            if (tileEntity.getPartAmount() > 0 && tileEntity.waterAmount > 0.0) {
                                // TODO: Reduce the stirs required by a fraction when there's fire under the cauldron
                                tileEntity.stirsRequired = stirsRequired / (tileEntity.getPartAmount() * tileEntity.waterAmount).toDouble()

                                if (tileEntity.stirAmount >= tileEntity.stirsRequired) {
                                    tileEntity.fullyStirred = true
                                    // println("Fully stirred!")
                                }
                                else {
                                    if (tileEntity.waterAmount <= tileEntity.maxWater) {
                                        tileEntity.stirAmount += 2 / tileEntity.waterAmount
                                    }
                                }
                            }
                        }
                    }
                    else {
                        when (itemStack.item) {
                            Items.STICK -> {
                                // Has a stirring stick
                                if (!tileEntity.hasStirrer) {
                                    tileEntity.hasStirrer = true
                                    itemStack.shrink(1)
                                }
                            }
                            Items.WATER_BUCKET -> {
                                // Fill the cauldron
                                tileEntity.increaseWater(1f)
                                itemStack.shrink(1)
                                playerIn.inventory.addItemStackToInventory(ItemStack(Items.BUCKET))

                                tileEntity.stirAmount = 0.0
                            }
                            Items.BUCKET -> {
                                // Empty the cauldron
                                if (tileEntity.waterAmount == tileEntity.maxWater) {
                                    itemStack.shrink(1)

                                    if (tileEntity.fullyStirred) {
                                        // TODO: Create a fluid for bug resonance
                                    }
                                    else {
                                        playerIn.inventory.addItemStackToInventory(ItemStack(Items.WATER_BUCKET))

                                        tileEntity.resetWater()
                                    }
                                }
                            }
                            Items.POTIONITEM -> {
                                if (itemStack.tagCompound?.getString("Potion") == "minecraft:water") {
                                    itemStack.shrink(1)
                                    playerIn.inventory.addItemStackToInventory(ItemStack(Items.GLASS_BOTTLE))

                                    tileEntity.increaseWater(0.1f)

                                    if (tileEntity.stirAmount - 3 > 0) {
                                        tileEntity.stirAmount -= 3
                                    }
                                    else {
                                        tileEntity.stirAmount = 0.0
                                    }
                                }
                            }
                            Items.GLASS_BOTTLE -> {
                                if (tileEntity.waterAmount > 0.1f) {
                                    if (tileEntity.fullyStirred) {
                                        itemStack.shrink(1)
                                        playerIn.inventory.addItemStackToInventory(ItemStack(ModItems.bugJuice))

                                        tileEntity.decreaseWater(0.1f)
                                    }
                                    else {
                                        itemStack.shrink(1)
                                        // TODO: Actually give a water bottle
                                        playerIn.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(ItemStack(Items.POTIONITEM), PotionTypes.WATER))

                                        tileEntity.decreaseWater(0.1f)
                                    }
                                }
                            }
                            is ItemBugPart -> {
                                // Add a part to the mix
                                if (tileEntity.getPartAmount() < tileEntity.maxParts) {
                                    // tileEntity.addPartAmout(1)
                                    tileEntity.addPart(itemStack.item as ItemBugPart)
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

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity {
        return TileEntityCauldron(8)
    }
}