/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

fun TileEntity.update(worldIn: World, block: Block, state: IBlockState) {
    worldIn.markBlockRangeForRenderUpdate(pos, pos)
    worldIn.notifyBlockUpdate(pos, state, state, 3)
    worldIn.scheduleBlockUpdate(pos, block, 0, 0)
    this.markDirty()
}
