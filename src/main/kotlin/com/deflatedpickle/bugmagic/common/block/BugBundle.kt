/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

class BugBundle : Generic("bug_bundle", CreativeTabs.DECORATIONS, Material.CLOTH, lightOpacity = 0, isFullBlock = false, isOpaqueCube = false, renderLayer = BlockRenderLayer.CUTOUT) {
    // TODO: Add more directions
    companion object {
        @JvmStatic
        val UP = PropertyBool.create("up")
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return 0
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, UP)
    }

    override fun canBeConnectedTo(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean {
        return when (facing) {
            EnumFacing.UP -> !world.isAirBlock(pos.offset(facing)) && world.getBlockState(pos).getBlockFaceShape(world, pos.offset(facing), facing) == BlockFaceShape.SOLID
            else -> false
        }
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        return state.withProperty(UP, canBeConnectedTo(worldIn, pos, EnumFacing.UP))
    }
}
