package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemGeneratorTileEntity
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class ImmobileServantTotemBlock : TotemBlock(
	"immobile_servant",
	Material.CACTUS,
	type = TotemType.GENERATOR,
	doesOutput = true,
	acceptsInput = true
) {
	override fun createTileEntity(
		world: World, state: IBlockState
	): TileEntity? = TotemGeneratorTileEntity(
		trades = mapOf(),
		upperBound = 144
	)
}
