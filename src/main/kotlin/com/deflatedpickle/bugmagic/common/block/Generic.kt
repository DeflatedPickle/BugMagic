package com.deflatedpickle.bugmagic.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

class Generic(name: String, creativeTab: CreativeTabs, materialIn: Material, mapColorIn: MapColor = materialIn.materialMapColor) : Block(materialIn, mapColorIn) {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab
    }
}