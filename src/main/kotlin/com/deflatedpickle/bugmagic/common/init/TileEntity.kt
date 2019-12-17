package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable
import net.minecraft.tileentity.TileEntity

object TileEntity {
    init {
        TileEntity.register("spell_table", SpellTable::class.java)
    }
}