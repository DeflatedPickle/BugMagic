package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.blocks.BlockAltar
import com.deflatedpickle.bugmagic.blocks.BlockBugJar
import com.deflatedpickle.bugmagic.blocks.BlockCauldron
import ladylib.registration.AutoRegister
import net.minecraft.block.Block

@AutoRegister(Reference.MOD_ID, injectObjectHolder = true)
object ModBlocks {
    @JvmField
    val BUG_JAR = BlockBugJar(1).init(1f, 1f, "pickaxe" to 0)
    @JvmField
    val CAULDRON = BlockCauldron(15).init(2f, 10f, "pickaxe" to 0)
    @JvmField
    val ALTAR = BlockAltar().init(2f, 10f, "pickaxe" to 0)

    fun <T : Block> T.init(hardness: Float, resistance: Float, harvestLevel: Pair<String, Int>): T {
        this.setHardness(hardness)
        this.setResistance(resistance)
        this.setHarvestLevel(harvestLevel.first, harvestLevel.second)
        return this
    }

    init {
        // Probably an easier way, but this creates a dud block to load the model
//        object : BlockMod("spell_parchment", Material.CARPET) {
//            override fun getModNamespace(): String {
//                return Reference.MOD_ID
//            }
//        }
    }
}