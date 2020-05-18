/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

object SmeltingInit {
    init {
        GameRegistry.addSmelting(BlockInit.BUG_BUNDLE, ItemStack(FoodInit.PETRIFIED_BUG_BUNDLE), 4f)
    }
}
