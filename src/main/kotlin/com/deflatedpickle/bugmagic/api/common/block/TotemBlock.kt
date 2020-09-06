package com.deflatedpickle.bugmagic.api.common.block

import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import com.deflatedpickle.bugmagic.api.common.util.extension.dropSlot
import com.deflatedpickle.bugmagic.api.common.util.extension.getSlotItems
import com.deflatedpickle.bugmagic.api.common.util.extension.update
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTableTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.ItemHandlerHelper

/**
 * A base for all totems to extend from
 * @see [TotemTileEntity]
 */
open class TotemBlock(
	name: String,
	material: Material,
	lightOpacity: Int = 0,
	val type: TotemType,
	val doesOutput: Boolean,
	val acceptsInput: Boolean
) : GenericBlock(
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
				"Type: ${
				this.type.name
					.toLowerCase()
					.capitalize()
				}"
			)
		)
	}

	// The totem will always need a TileEntity to function
	override fun hasTileEntity(state: IBlockState): Boolean = true

	// The default totem tile entity should be good enough
	override fun createTileEntity(
		world: World, state: IBlockState
	): TileEntity? = TotemTileEntity(
		totemType = this.type
	)

	// Insert/output items
	override fun onBlockActivated(
		worldIn: World,
		pos: BlockPos,
		state: IBlockState,
		playerIn: EntityPlayer,
		hand: EnumHand,
		facing: EnumFacing,
		hitX: Float, hitY: Float, hitZ: Float
	): Boolean {
		if (!worldIn.isRemote) {
			val tileEntity = worldIn.getTileEntity(pos)
			val stack = playerIn.getHeldItem(hand)

			if (tileEntity is TotemTileEntity) {
				val itemCount = 0.until(tileEntity.outputItemStackHandler.slots).count {
					tileEntity.outputItemStackHandler.getStackInSlot(it) !=
						ItemStack.EMPTY
				}

				if (this.doesOutput) {
					if (playerIn.isSneaking &&
						stack == ItemStack.EMPTY) {
						tileEntity.outputItemStackHandler.dropSlot(
							itemCount - 1, worldIn, pos
						)
						tileEntity.update(worldIn, this, state)
					}
				}

				if (this.acceptsInput) {
					if (stack != ItemStack.EMPTY) {
						ItemHandlerHelper.insertItemStacked(
							tileEntity.inputItemStackHandler,
							stack.splitStack(1),
							false)
						tileEntity.update(worldIn, this, state)

						println(tileEntity.inputItemStackHandler.getSlotItems())
					}
				}
			}
		}

		return true
	}

	override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
		val tileEntity = worldIn.getTileEntity(pos)

		if (tileEntity is TotemTileEntity) {
			for (inventory in arrayOf(
				tileEntity.inputItemStackHandler,
				tileEntity.outputItemStackHandler
			)) {
				for (index in 0 until tileEntity.outputItemStackHandler.slots) {
					val stack = tileEntity.outputItemStackHandler.getStackInSlot(index)

					if (stack != ItemStack.EMPTY) {
						val entity = EntityItem(worldIn,
							pos.x.toDouble(), pos.y.toDouble() + 1, pos.z.toDouble(),
							stack
						)
						worldIn.spawnEntity(entity)
					}
				}
			}
		}

		super.breakBlock(worldIn, pos, state)
	}
}
