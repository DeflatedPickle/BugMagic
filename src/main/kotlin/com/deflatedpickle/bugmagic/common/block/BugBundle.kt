/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.IBoundingBox
import com.deflatedpickle.bugmagic.api.common.block.Generic
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

class BugBundle : Generic("bug_bundle", CreativeTabs.DECORATIONS, Material.CLOTH, lightOpacity = 0, isFullBlock = false, isOpaqueCube = false, renderLayer = BlockRenderLayer.CUTOUT),
        IBoundingBox {
    // TODO: Add more directions
    companion object {
        @JvmStatic
        val UP = PropertyBool.create("up")

        val EMPTY_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val bbUnit = 1.0 / 16.0

        val topAABB = AxisAlignedBB(
                bbUnit * 7, bbUnit * 8.05, bbUnit * 7,
                bbUnit * 9, bbUnit * 11, bbUnit * 9
        )
        val secondTopAABB = AxisAlignedBB(
                bbUnit * 5, bbUnit * 7.05, bbUnit * 5,
                bbUnit * 11, bbUnit * 8, bbUnit * 11
        )
        val middleAABB = AxisAlignedBB(
                bbUnit * 4, bbUnit * 4, bbUnit * 4,
                bbUnit * 12, bbUnit * 7, bbUnit * 12
        )
        val thirdBottomAABB = AxisAlignedBB(
                bbUnit * 5, bbUnit * 2, bbUnit * 5,
                bbUnit * 11, bbUnit * 3.95, bbUnit * 11
        )
        val secondBottomAABB = AxisAlignedBB(
                bbUnit * 6, bbUnit, bbUnit * 6,
                bbUnit * 10, bbUnit * 2.95, bbUnit * 10
        )
        val bottomAABB = AxisAlignedBB(
                bbUnit * 7, 0.0, bbUnit * 7,
                bbUnit * 9, bbUnit * 1.95, bbUnit * 9
        )

        val boundingBoxes = listOf(
                topAABB,
                secondTopAABB,
                middleAABB,
                thirdBottomAABB,
                secondBottomAABB,
                bottomAABB
        )
    }

    override fun getMetaFromState(state: IBlockState): Int = 0

    override fun createBlockState(): BlockStateContainer = BlockStateContainer(this, UP)

    override fun canBeConnectedTo(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean {
        return when (facing) {
            // EnumFacing.UP -> !world.isAirBlock(pos.offset(facing)) && world.getBlockState(pos).getBlockFaceShape(world, pos.offset(facing), facing) == BlockFaceShape.SOLID
            else -> false
        }
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState = state.withProperty(UP, canBeConnectedTo(worldIn, pos, EnumFacing.UP))

    override fun getSelectedBoundingBox(state: IBlockState, worldIn: World, pos: BlockPos): AxisAlignedBB? = EMPTY_AABB

    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean) = boundingBoxList.forEach { addCollisionBoxToList(pos, entityBox, collidingBoxes, it) }

    override fun getBoundingBoxList(): List<AxisAlignedBB> = boundingBoxes
}
