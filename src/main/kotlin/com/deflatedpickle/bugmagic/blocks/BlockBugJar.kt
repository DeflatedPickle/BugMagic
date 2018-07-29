package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.items.ItemBugNet
import com.deflatedpickle.bugmagic.tileentity.TileEntityBugJar
import com.deflatedpickle.picklelib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.apache.commons.lang3.tuple.ImmutablePair

class BlockBugJar(name: String, val maxBugs: Int) : BlockBase(name, Material.GLASS, 1f, 1f, ImmutablePair("pickaxe", 0), ModCreativeTabs.tabGeneral), ITileEntityProvider {
    // TODO: Fix the bounding box (it appears to move with the player, and the player doesn't collide with it)
    // TODO: Fix the model and textures

    private val axisAlignedBB = AxisAlignedBB(6.0 / 16, 0.0, 6.0 / 16, 6.0 / 10, 6.0 / 16, 6.0 / 10)

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return axisAlignedBB
    }

    override fun addCollisionBoxToList(state: IBlockState?, worldIn: World?, pos: BlockPos?, entityBox: AxisAlignedBB?, collidingBoxes: MutableList<AxisAlignedBB>?, entityIn: Entity?, isActualState: Boolean) {
        super.addCollisionBoxToList(state, worldIn, pos, axisAlignedBB, collidingBoxes, entityIn, isActualState)
    }

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn!!.getTileEntity(pos)

        if (worldIn.isRemote) {
            if (tileEntity is TileEntityBugJar) {
                if (hand == EnumHand.MAIN_HAND) {
                    if (playerIn!!.getHeldItem(hand).item is ItemBugNet) {
                        // Remove bugs from the net
                        val stackCompound = playerIn.getHeldItem(hand).tagCompound!!

                        if (stackCompound.getInteger("bugs") >= 0 && tileEntity.getBugs() < maxBugs) {
                            stackCompound.setInteger("bugs", stackCompound.getInteger("bugs") - 1)

                            tileEntity.addBug(1)
                        }
                    }
                    else if (playerIn.getHeldItem(hand).isEmpty) {
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

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TileEntityBugJar(maxBugs)
    }
}