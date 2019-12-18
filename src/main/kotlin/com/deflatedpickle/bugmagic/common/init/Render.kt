/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.client.render.entity.EssenceCollector as EssenceCollectorRender
import com.deflatedpickle.bugmagic.client.render.entity.ItemCollector as ItemCollectorRender
import com.deflatedpickle.bugmagic.client.render.tileentity.SpellTable as SpellTableRender
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollector
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollector
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry

object Render {
    init {
        RenderingRegistry.registerEntityRenderingHandler(ItemCollector::class.java, ::ItemCollectorRender)
        ItemCollectorRender.registerModels()

        RenderingRegistry.registerEntityRenderingHandler(EssenceCollector::class.java, ::EssenceCollectorRender)
        EssenceCollectorRender.registerModels()

        ClientRegistry.bindTileEntitySpecialRenderer(SpellTable::class.java, SpellTableRender())

        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(Block.BUG_ESSENCE), ItemMeshDefinition {
            return@ItemMeshDefinition ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "bug_essence"), "fluid")
        })

        ModelLoader.setCustomStateMapper(Block.BUG_ESSENCE, object : StateMapperBase() {
            override fun getModelResourceLocation(state: IBlockState): ModelResourceLocation {
                return ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "bug_essence"), "fluid")
            }
        })
    }
}
