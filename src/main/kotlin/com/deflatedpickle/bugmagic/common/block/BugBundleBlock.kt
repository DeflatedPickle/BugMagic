/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.BoundingBox
import com.deflatedpickle.bugmagic.api.common.block.GenericBlock
import com.deflatedpickle.bugmagic.api.common.util.AABBUtil
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

/**
 * A decoration block bundle of bugs, wrapped in string
 *
 * @author DeflatedPickle
 */
class BugBundleBlock : GenericBlock(
        "bug_bundle",
        CreativeTabs.DECORATIONS,
        Material.CLOTH,
        lightOpacity = 0,
        isFullBlock = false,
        isOpaqueCube = false,
        renderLayer = BlockRenderLayer.CUTOUT
),
        BoundingBox {
    // TODO: Add more directions
    companion object {
        @JvmStatic
        val UP = PropertyBool.create("up")

        val topAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 7, AABBUtil.unitDouble() * 8.05, AABBUtil.unitDouble() * 7,
                AABBUtil.unitDouble() * 9, AABBUtil.unitDouble() * 11, AABBUtil.unitDouble() * 9
        )
        val secondTopAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 5, AABBUtil.unitDouble() * 7.05, AABBUtil.unitDouble() * 5,
                AABBUtil.unitDouble() * 11, AABBUtil.unitDouble() * 8, AABBUtil.unitDouble() * 11
        )
        val middleAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 4, AABBUtil.unitDouble() * 4, AABBUtil.unitDouble() * 4,
                AABBUtil.unitDouble() * 12, AABBUtil.unitDouble() * 7, AABBUtil.unitDouble() * 12
        )
        val thirdBottomAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 5, AABBUtil.unitDouble() * 2, AABBUtil.unitDouble() * 5,
                AABBUtil.unitDouble() * 11, AABBUtil.unitDouble() * 3.95, AABBUtil.unitDouble() * 11
        )
        val secondBottomAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 6, AABBUtil.unitDouble(), AABBUtil.unitDouble() * 6,
                AABBUtil.unitDouble() * 10, AABBUtil.unitDouble() * 2.95, AABBUtil.unitDouble() * 10
        )
        val bottomAABB = AxisAlignedBB(
                AABBUtil.unitDouble() * 7, 0.0, AABBUtil.unitDouble() * 7,
                AABBUtil.unitDouble() * 9, AABBUtil.unitDouble() * 1.95, AABBUtil.unitDouble() * 9
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

    override fun canBeConnectedTo(
        world: IBlockAccess,
        pos: BlockPos,
        facing: EnumFacing
    ): Boolean {
        return when (facing) {
            // EnumFacing.UP -> !world.isAirBlock(pos.offset(facing)) && world.getBlockState(pos).getBlockFaceShape(world, pos.offset(facing), facing) == BlockFaceShape.SOLID
            else -> false
        }
    }

    override fun getActualState(
        state: IBlockState,
        worldIn: IBlockAccess,
        pos: BlockPos
    ): IBlockState =
            state.withProperty(UP, canBeConnectedTo(worldIn, pos, EnumFacing.UP))

    override fun getSelectedBoundingBox(
        state: IBlockState,
        worldIn: World,
        pos: BlockPos
    ): AxisAlignedBB? = AABBUtil.empty()

    override fun addCollisionBoxToList(
        state: IBlockState,
        worldIn: World,
        pos: BlockPos,
        entityBox: AxisAlignedBB,
        collidingBoxes: MutableList<AxisAlignedBB>,
        entityIn: Entity?,
        isActualState: Boolean
    ) = boundingBoxList.forEach { addCollisionBoxToList(pos, entityBox, collidingBoxes, it) }

    override fun getBoundingBoxList(): List<AxisAlignedBB> = boundingBoxes
}
