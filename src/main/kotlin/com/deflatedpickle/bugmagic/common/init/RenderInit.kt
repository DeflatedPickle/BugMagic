/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.client.render.tileentity.TotemTileEntitySpecialRender
import com.deflatedpickle.bugmagic.api.common.block.tileentity.TotemTileEntity
import com.deflatedpickle.bugmagic.client.render.entity.AutoFertilizerRender
import com.deflatedpickle.bugmagic.client.render.entity.AutoHarvesterRender
import com.deflatedpickle.bugmagic.client.render.entity.AutoHoeRender
import com.deflatedpickle.bugmagic.client.render.entity.AutoPlanterRender
import com.deflatedpickle.bugmagic.client.render.entity.EssenceCollectorRender
import com.deflatedpickle.bugmagic.client.render.entity.ItemCollectorRender
import com.deflatedpickle.bugmagic.client.render.tileentity.SpellTableTileEntitySpecialRender
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTableTileEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoFertilizerEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHarvesterEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoHoeEntity
import com.deflatedpickle.bugmagic.common.entity.mob.AutoPlanterEntity
import com.deflatedpickle.bugmagic.common.entity.mob.EssenceCollectorEntity
import com.deflatedpickle.bugmagic.common.entity.mob.ItemCollectorEntity
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry

object RenderInit {
    init {
        // Entities
        RenderingRegistry.registerEntityRenderingHandler(ItemCollectorEntity::class.java, ::ItemCollectorRender)
        ItemCollectorRender.registerModels()

        RenderingRegistry.registerEntityRenderingHandler(EssenceCollectorEntity::class.java, ::EssenceCollectorRender)
        EssenceCollectorRender.registerModels()

        RenderingRegistry.registerEntityRenderingHandler(AutoFertilizerEntity::class.java, ::AutoFertilizerRender)
        RenderingRegistry.registerEntityRenderingHandler(AutoHarvesterEntity::class.java, ::AutoHarvesterRender)
        RenderingRegistry.registerEntityRenderingHandler(AutoHoeEntity::class.java, ::AutoHoeRender)
        RenderingRegistry.registerEntityRenderingHandler(AutoPlanterEntity::class.java, ::AutoPlanterRender)

        // TESRs
        ClientRegistry.bindTileEntitySpecialRenderer(SpellTableTileEntity::class.java, SpellTableTileEntitySpecialRender())
		ClientRegistry.bindTileEntitySpecialRenderer(TotemTileEntity::class.java, TotemTileEntitySpecialRender())

        // Fluid
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BlockInit.BUG_ESSENCE), ItemMeshDefinition {
            return@ItemMeshDefinition ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "bug_essence"), "fluid")
        })

        ModelLoader.setCustomStateMapper(BlockInit.BUG_ESSENCE, object : StateMapperBase() {
            override fun getModelResourceLocation(state: IBlockState): ModelResourceLocation {
                return ModelResourceLocation(ResourceLocation(Reference.MOD_ID, "bug_essence"), "fluid")
            }
        })
    }
}
