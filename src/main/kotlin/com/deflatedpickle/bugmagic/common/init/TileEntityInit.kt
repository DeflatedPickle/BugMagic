/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemGathererTileEntity
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemGeneratorTileEntity
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTableTileEntity
import net.minecraft.tileentity.TileEntity

object TileEntityInit {
    init {
        TileEntity.register("spell_table", SpellTableTileEntity::class.java)

		TileEntity.register("totem", TotemTileEntity::class.java)
		TileEntity.register("totem_gatherer", TotemGathererTileEntity::class.java)
		TileEntity.register("totem_generator", TotemGeneratorTileEntity::class.java)
    }
}
