package com.deflatedpickle.bugmagic.common.init

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

object FurnaceRecipe {
    init {
        GameRegistry.addSmelting(Block.BUG_BUNDLE, ItemStack(Food.PETRIFIED_BUG_BUNDLE), 4f)
    }
}