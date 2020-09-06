package com.deflatedpickle.bugmagic.common.block

import com.deflatedpickle.bugmagic.api.common.block.GenericBlock
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.BlockRenderLayer

// it's in your head
// in your head
// zombie
class HeadOfPig : GenericBlock(
	"pig_head",
	materialIn = Material.CACTUS,
	creativeTab = CreativeTabs.DECORATIONS,
	isFullBlock = false,
	isOpaqueCube = false,
	renderLayer = BlockRenderLayer.CUTOUT
)
