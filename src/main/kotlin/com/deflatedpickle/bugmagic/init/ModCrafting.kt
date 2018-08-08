package com.deflatedpickle.bugmagic.init

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object ModCrafting {
    init {
        // Items
        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:generic_wand_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModItems.wandGeneric),
                "  S",
                " S ",
                "S  ",
                'S', Items.STICK)

        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:bug_net_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModItems.bugNet),
                " WS",
                " WS",
                "W  ",
                'W', Items.STICK, 'S', Items.STRING)

        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:magnifying_glass_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModItems.magnifyingGlass),
                "  G",
                " S ",
                "S  ",
                'G', Blocks.GLASS, 'S', Items.STICK)

        // Blocks
        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:bug_jar_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModBlocks.bugJar),
                " W ",
                "G G",
                "GGG",
                'W', Blocks.PLANKS, 'G', Blocks.GLASS)

        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:cauldron_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModBlocks.cauldron),
                "I I",
                "I I",
                "IBI",
                'I', Items.IRON_INGOT, 'B', Blocks.IRON_BLOCK)

        GameRegistry.addShapedRecipe(ResourceLocation("bugmagic:altar_recipe"), ResourceLocation("bugmagic"),
                ItemStack(ModBlocks.altar),
                "SSS",
                " W ",
                "SSS",
                'S', Blocks.STONE_SLAB, 'W', Blocks.PLANKS)
    }
}