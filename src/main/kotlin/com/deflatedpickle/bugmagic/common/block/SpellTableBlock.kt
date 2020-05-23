/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.api.BoundingBox
import com.deflatedpickle.bugmagic.api.common.block.GenericBlock
import com.deflatedpickle.bugmagic.api.common.util.extension.drop
import com.deflatedpickle.bugmagic.api.common.util.extension.dropSlot
import com.deflatedpickle.bugmagic.api.common.util.extension.getSlotItems
import com.deflatedpickle.bugmagic.api.common.util.extension.isNotEmpty
import com.deflatedpickle.bugmagic.api.common.util.extension.update
import com.deflatedpickle.bugmagic.api.spell.SpellRecipe
import com.deflatedpickle.bugmagic.client.render.tileentity.SpellTableTileEntitySpecialRender
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTableTileEntity
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.init.FoodInit
import com.deflatedpickle.bugmagic.common.init.SpellRecipeInit
import com.deflatedpickle.bugmagic.common.item.Wand
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellChange
import java.util.concurrent.ThreadLocalRandom
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.items.ItemHandlerHelper

/**
 * The block for the tile entity [SpellTableTileEntity] and the renderer [SpellTableTileEntitySpecialRender]
 */
class SpellTableBlock : GenericBlock("spell_table", CreativeTabs.DECORATIONS, Material.WOOD, lightOpacity = 0, isFullBlock = false, isOpaqueCube = false, renderLayer = BlockRenderLayer.CUTOUT),
	BoundingBox {
	init {
		setHardness(4f)
	}

	companion object {
		val tableAABB = AxisAlignedBB(
			0.0, 0.0, 0.1,
			1.0, 0.8, 0.9
		)

		val liquidAABB = AxisAlignedBB(
			1.0, 0.5, 0.5,
			1.2, 0.95, 0.8
		)

		val wandAABB = AxisAlignedBB(
			1.0, 0.45, 0.19,
			1.13, 0.9, 0.44
		)

		val matAABB = AxisAlignedBB(
			0.2, 0.82, 0.2,
			0.8, 0.9, 0.6
		)

		val inkAABB = AxisAlignedBB(
			0.03, 0.82, 0.6,
			0.28, 1.0, 0.8
		)
	}

	override fun getSelectedBoundingBox(state: IBlockState, worldIn: World, pos: BlockPos): AxisAlignedBB = tableAABB.offset(pos)

	override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, liquidAABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, wandAABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, matAABB)
	}

	override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!worldIn.isRemote) {
			val tileEntity = worldIn.getTileEntity(pos)

			if (tileEntity is SpellTableTileEntity) {
				val itemCount = 0.until(tileEntity.itemStackHandler.slots).count { tileEntity.itemStackHandler.getStackInSlot(it) != ItemStack.EMPTY }
				val stack = playerIn.getHeldItem(hand)

				if (playerIn.canPlayerEdit(pos, facing, stack)) {
					val hitVector = Vec3d(hitX.toDouble(), hitY.toDouble(), hitZ.toDouble())

					// Put liquid in
					if (liquidAABB.grow(0.2).contains(hitVector)) {
						if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
							var result = FluidUtil.tryEmptyContainer(stack, tileEntity.fluidTank, Fluid.BUCKET_VOLUME, playerIn, true)

							if (!result.success) {
								result = FluidUtil.tryFillContainer(stack, tileEntity.fluidTank, Fluid.BUCKET_VOLUME, playerIn, true)
							}

							if (result.isSuccess) {
								playerIn.setHeldItem(hand, result.getResult())
								tileEntity.recipeProgression = 0f
							}

							tileEntity.update(worldIn, this, state)
						}
					}
					// Put a wand in
					else if (wandAABB.grow(0.2).contains(hitVector)) {
						if (playerIn.isSneaking) {
							tileEntity.wandStackHandler.dropSlot(0, worldIn, pos)
							tileEntity.update(worldIn, this, state)
						} else if (stack.item is Wand) {
							tileEntity.wandStackHandler.insertItem(0, stack.splitStack(1), false)
							tileEntity.update(worldIn, this, state)
						}
					}
					// Put a feather or ink in
					else if (inkAABB.grow(0.2).contains(hitVector)) {
						if (playerIn.isSneaking) {
							tileEntity.featherStackHandler.dropSlot(0, worldIn, pos)
							tileEntity.recipeProgression = 0f
							tileEntity.update(worldIn, this, state)
						} else {
							when (stack.item) {
								Items.FEATHER -> {
									tileEntity.featherStackHandler.insertItem(0, stack.splitStack(1), false)
									tileEntity.update(worldIn, this, state)
								}
								Items.DYE -> {
									if (stack.metadata == 0 && tileEntity.ink < 1f) {
										stack.shrink(1)

										tileEntity.ink = 1f
										tileEntity.update(worldIn, this, state)
									}
								}
							}
						}
					}
					// Put items in
					else if (matAABB.grow(0.2).contains(hitVector)) {
						// TODO: Track where the items should be, then allow clicking on them to take them out
						if (playerIn.isSneaking) {
							if (itemCount - 1 >= 0) {
								tileEntity.itemStackHandler.dropSlot(itemCount - 1, worldIn, pos)
								tileEntity.recipeProgression = 0f
								tileEntity.update(worldIn, this, state)
							}
						} else {
							if (stack.isNotEmpty() && stack.item !is Wand) {
								ItemHandlerHelper.insertItemStacked(tileEntity.itemStackHandler, stack.splitStack(1), false)
								tileEntity.recipeProgression = 0f
								tileEntity.update(worldIn, this, state)
							}
						}
					} else {
						if (stack.item is Wand) {
							val split = tileEntity.validRecipe.split(":")
							if (split.size <= 1) return false

							val resourceLocation = ResourceLocation(split[0], split[1])

							if (split.size > 1) {
								val recipe = GameRegistry.findRegistry(SpellRecipe::class.java).getValue(resourceLocation)!!

								recipe.ingredients?.let { ingredients ->
									if (tileEntity.featherStackHandler.getStackInSlot(0).item == Items.FEATHER &&
										tileEntity.ink - recipe.inkAmount > 0 &&
										tileEntity.fluidTank.fluid?.fluid?.unlocalizedName == recipe.fluidType &&
										tileEntity.fluidTank.fluidAmount - recipe.fluidAmount > 0) {
										if (Math.round(tileEntity.recipeProgression * 100f) / 100f >= 1f) {
											tileEntity.ink -= recipe.inkAmount
											tileEntity.fluidTank.drain(recipe.fluidAmount, true)

											tileEntity.recipeProgression = 0f

											val allItemStacks = tileEntity.itemStackHandler.getSlotItems().toMutableList()
											val filteredStacks = allItemStacks.filter { stack ->
												stack.item in ingredients.map {
													it.item
												}
											}

											SpellLearnerCapability.isCapable(playerIn)?.let {
												it.learnSpell(recipe.spell)
												// You HAVE to send THIS packet
												// Otherwise the client WON'T be notified of the change
												// And the player will NOT be able to cast the spell
												BugMagic.CHANNEL.sendToAll(MessageSpellChange(playerIn.entityId, it.spellList))
											}

											for (i in filteredStacks) {
												for (l in 0 until i.count) {
													i.shrink(1)
													if (ThreadLocalRandom.current().nextInt(0, 10) == 0) {
														ItemStack(FoodInit.JELLY_BUG).drop(worldIn, pos)
													}
												}
											}
											tileEntity.update(worldIn, this, state)
										} else {
											tileEntity.recipeProgression += 1f / recipe.craftingTime
											tileEntity.update(worldIn, this, state)
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return true
	}

	override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
		val tileEntity = worldIn.getTileEntity(pos)

		if (tileEntity is SpellTableTileEntity) {
			for (i in 0 until tileEntity.itemStackHandler.slots) {
				val stack = tileEntity.itemStackHandler.getStackInSlot(i)

				if (stack != ItemStack.EMPTY) {
					val entity = EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble() + 1, pos.z.toDouble(), stack)
					worldIn.spawnEntity(entity)
				}
			}
		}

		super.breakBlock(worldIn, pos, state)
	}

	override fun hasTileEntity(state: IBlockState): Boolean = true
	override fun createTileEntity(world: World, state: IBlockState): TileEntity? = SpellTableTileEntity()

	override fun getBoundingBoxList(): List<AxisAlignedBB> = mutableListOf(
		liquidAABB,
		wandAABB,
		matAABB,
		inkAABB
	)
}
