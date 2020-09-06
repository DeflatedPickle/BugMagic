package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.BoundingBox
import com.deflatedpickle.bugmagic.api.TotemType
import com.deflatedpickle.bugmagic.api.common.block.TotemBlock
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemGathererTileEntity
import com.deflatedpickle.bugmagic.api.common.util.AABBUtil
import com.deflatedpickle.bugmagic.common.init.ItemInit
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

// TODO: Add some rotting mechanic before producing flies
class CrownOfTheLordTotemBlock : TotemBlock(
	"pig_stick",
	Material.CACTUS,
	type = TotemType.GATHERER,
	doesOutput = true,
	acceptsInput = false
), BoundingBox {
	companion object {
		val stickAABB = AxisAlignedBB(
			AABBUtil.unitDouble() * 7, 0.0, AABBUtil.unitDouble() * 7,
			AABBUtil.unitDouble() * 9, AABBUtil.unitDouble() * 14, AABBUtil.unitDouble() * 9
		)

		val headAABB = AxisAlignedBB(
			AABBUtil.unitDouble() * 5.5, AABBUtil.unitDouble() * 13.5, AABBUtil.unitDouble() * 5.5,
			AABBUtil.unitDouble() * 10.5, AABBUtil.unitDouble() * 18.5, AABBUtil.unitDouble() * 10.5
		)

		val noseAABB = AxisAlignedBB(
			AABBUtil.unitDouble() * 6.5, AABBUtil.unitDouble() * 13.5, AABBUtil.unitDouble() * 5.5,
			AABBUtil.unitDouble() * 9.5, AABBUtil.unitDouble() * 15.5, AABBUtil.unitDouble() * 4.5
		)

		val leftEarAABB = AxisAlignedBB(
			AABBUtil.unitDouble() * 5.5, AABBUtil.unitDouble() * 13.5, AABBUtil.unitDouble() * 9.5,
			AABBUtil.unitDouble() * 4.5, AABBUtil.unitDouble() * 17.5, AABBUtil.unitDouble() * 6.5
		)

		val rightEarAABB = AxisAlignedBB(
			AABBUtil.unitDouble() * 10.5, AABBUtil.unitDouble() * 13.5, AABBUtil.unitDouble() * 9.5,
			AABBUtil.unitDouble() * 11.5, AABBUtil.unitDouble() * 17.5, AABBUtil.unitDouble() * 6.5
		)
	}

	override fun getBoundingBox(
		state: IBlockState, source: IBlockAccess, pos: BlockPos
	): AxisAlignedBB = stickAABB

	override fun getBoundingBoxList(): List<AxisAlignedBB> = listOf(
		headAABB,
		noseAABB,
		leftEarAABB,
		rightEarAABB
	)

	override fun addCollisionBoxToList(
		state: IBlockState,
		worldIn: World,
		pos: BlockPos,
		entityBox: AxisAlignedBB,
		collidingBoxes:
		MutableList<AxisAlignedBB>,
		entityIn: Entity?,
		isActualState: Boolean
	) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, headAABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, noseAABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, leftEarAABB)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, rightEarAABB)
	}

	override fun createTileEntity(
		world: World, state: IBlockState
	): TileEntity? = TotemGathererTileEntity(
		gather = { ItemInit.BUG },
		upperBound = 99
	)
}
