package com.deflatedpickle.bugmagic.api.common.block

import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.world.World

/**
 * A base for all totems to extend from
 * @see [TotemTileEntity]
 */
open class TotemBlock(name: String, material: Material, lightOpacity: Int = 0, val type: TotemType) : GenericBlock(
	name = name,
	// Should you be able to specify the tab?
	creativeTab = CreativeTabs.DECORATIONS,
	materialIn = material,
	lightOpacity = lightOpacity,
	// Totems are probably not going to be full blocks
	// But it might be, so we won't implement BoundingBox
	isFullBlock = false,
	isOpaqueCube = false,
	renderLayer = BlockRenderLayer.CUTOUT
) {
	override fun addInformation(
		stack: ItemStack, worldIn: World?,
		tooltip: MutableList<String>, flagIn: ITooltipFlag
	) {
		tooltip.addAll(
			arrayOf(
				"Type: ${this.type.name.toLowerCase().capitalize()}"
			)
		)
	}

	// The totem will always need a TileEntity to function
	override fun hasTileEntity(state: IBlockState): Boolean = true

	// The default totem tile entity should be good enough
	override fun createTileEntity(
		world: World, state: IBlockState
	): TileEntity? = TotemTileEntity(totemType = this.type)
}
