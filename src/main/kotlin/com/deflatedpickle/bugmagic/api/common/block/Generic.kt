/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

open class Generic(
    name: String,
    creativeTab: CreativeTabs?,
    materialIn: Material,
    mapColorIn: MapColor = materialIn.materialMapColor,
    soundType: SoundType = SoundType.STONE,
    lightOpacity: Int = 15,
    lightLevel: Float = 0f,
    blastResistance: Float = 1f,
    private val normalCube: Boolean = true,
    private val topSolid: Boolean = true,
    private val isFullBlock: Boolean = true,
    private val isOpaqueCube: Boolean = true,
    private val renderLayer: BlockRenderLayer = BlockRenderLayer.SOLID,
    private val stickyBlock: Boolean = false,
    private val blockFaceShape: BlockFaceShape = BlockFaceShape.SOLID
) :
    Block(materialIn, mapColorIn) {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab

        this.soundType = soundType
        if (lightOpacity > 0f) this.setLightOpacity(lightOpacity)
        if (lightLevel > 0) this.setLightLevel(lightLevel)
        if (blastResistance != 1f) this.blockResistance = blastResistance
    }

    override fun isBlockNormalCube(state: IBlockState): Boolean {
        return this.normalCube
    }

    override fun isTopSolid(state: IBlockState): Boolean {
        return this.topSolid
    }

    override fun isFullBlock(state: IBlockState): Boolean {
        return this.isFullBlock
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return this.isFullBlock
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return this.isOpaqueCube
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return this.renderLayer
    }

    override fun isStickyBlock(state: IBlockState): Boolean {
        return this.stickyBlock
    }

    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape {
        return this.blockFaceShape
    }
}
