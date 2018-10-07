package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.tileentity.TileEntityAltar
import com.deflatedpickle.bugmagic.util.AltarUtil
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockAltar : Block(Material.IRON){
    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn.getTileEntity(pos)

        if (!worldIn.isRemote) {
            if (tileEntity is TileEntityAltar) {
                if (playerIn.canPlayerEdit(pos, facing, playerIn.getHeldItem(hand))) {
                    if (hand == EnumHand.MAIN_HAND) {
                        val itemStack = playerIn.getHeldItem(hand)

                        if (itemStack.isEmpty) {
                            if (playerIn.isSneaking) {
                                // Throw the parts back into the world
                                for (i in tileEntity.getParts()) {
                                    worldIn.spawnEntity(EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(i)))
                                }
                                tileEntity.clearParts()
                            }
                            else {
                                // Attempt to craft the items together
                                if (AltarUtil.getRecipes().containsKey(tileEntity.getParts())) {
                                    // The recipe's valid, poof out the item, please
                                    val item = EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(AltarUtil.getRecipes()[tileEntity.getParts()]!!))
                                    worldIn.spawnEntity(item)
                                }

                            }
                        }
                        else if (itemStack.item is Item) {
                            tileEntity.addPart(itemStack.item)
                            itemStack.shrink(1)
                        }
                    }
                }
            }
        }

        return true
    }

    override fun hasTileEntity(state: IBlockState?): Boolean {
        return true
    }

    override fun createTileEntity(world: World?, state: IBlockState?): TileEntity? {
        return TileEntityAltar(16)
    }
}