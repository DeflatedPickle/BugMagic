package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.tileentity.TileEntityBugJar
import net.minecraftforge.fml.common.registry.GameRegistry

object ModTileEntities {
    init {
        GameRegistry.registerTileEntity(TileEntityBugJar::class.java, Reference.MOD_ID + "tileentity_bug_jar")
    }
}