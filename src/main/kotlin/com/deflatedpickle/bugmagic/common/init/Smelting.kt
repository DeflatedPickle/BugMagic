/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

object Smelting {
    init {
        GameRegistry.addSmelting(Block.BUG_BUNDLE, ItemStack(Food.PETRIFIED_BUG_BUNDLE), 4f)
    }
}
