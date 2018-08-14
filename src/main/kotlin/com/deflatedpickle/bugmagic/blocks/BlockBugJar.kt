package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.items.ItemBugNet
import com.deflatedpickle.bugmagic.items.ItemMagnifyingGlass
import com.deflatedpickle.bugmagic.tileentity.TileEntityBugJar
import com.deflatedpickle.picklelib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.tuple.ImmutablePair

class BlockBugJar(name: String, private val maxBugs: Int) : BlockBase(name, Material.GLASS, 1f, 1f, ImmutablePair("pickaxe", 0), ModCreativeTabs.tabGeneral), ITileEntityProvider {
    // TODO: Fix the bounding box (it appears to move with the player, and the player doesn't collide with it)

    private val axisAlignedBB = AxisAlignedBB(6.0 / 16, 0.0, 6.0 / 16, 6.0 / 10, 6.0 / 16, 6.0 / 10)

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return axisAlignedBB
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn.getTileEntity(pos)

        if (worldIn.isRemote) {
            if (tileEntity is TileEntityBugJar) {
                if (hand == EnumHand.MAIN_HAND) {
                    val itemStack = playerIn!!.getHeldItem(hand)
                    if (itemStack.item is ItemBugNet) {
                        // Remove bugs from the net
                        val stackCompound = itemStack.tagCompound!!

                        if (stackCompound.getInteger("bugs") >= 0 && tileEntity.getBugs() < maxBugs) {
                            stackCompound.setInteger("bugs", stackCompound.getInteger("bugs") - 1)

                            tileEntity.addBug(1)
                        }
                    }
                    else if (itemStack.item is ItemMagnifyingGlass) {
                        if (tileEntity.getBugs() > 0) {
                            itemStack.itemDamage = itemStack.itemDamage - 1

                            val bugParts = RandomUtils.nextInt(1, 2)

                            for (i in 0..bugParts) {
                                val partType = ModItems.bugParts.shuffled().last()

                                playerIn.inventory.addItemStackToInventory(ItemStack(partType))
                            }

                            tileEntity.removeBug()
                        }
                    }
                    else if (itemStack.isEmpty) {
                        if (playerIn.isSneaking) {
                            tileEntity.clearBugs()
                        }
                        else {
                            tileEntity.removeBug()
                        }
                    }
                }
            }
        }

        return true
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity {
        return TileEntityBugJar(maxBugs)
    }
}