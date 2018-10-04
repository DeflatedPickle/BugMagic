package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.blocks.BlockAltar
import com.deflatedpickle.bugmagic.blocks.BlockBugJar
import com.deflatedpickle.bugmagic.blocks.BlockCauldron
import net.minecraft.block.material.Material
import vazkii.arl.block.BlockMod

object ModBlocks {
    val bugJar = BlockBugJar("bug_jar", 1)
    val cauldron = BlockCauldron("cauldron", 65)
    val altar = BlockAltar("altar")

    init {
        // Probably an easier way, but this creates a dud block to load the model
        object : BlockMod("spell_parchment", Material.CARPET) {
            override fun getModNamespace(): String {
                return Reference.MOD_ID
            }
        }
    }
}