package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.util.TextureUtil
import net.minecraft.util.ResourceLocation

object ModTextures {
    private val waterStill = ResourceLocation("minecraft:textures/blocks/water_still.png")
    private val waterFlow = ResourceLocation("minecraft:textures/blocks/water_flow.png")

    val bugEssenceStillLocation = ResourceLocation("bugmagic:textures/blocks/bug_essence_still.png")
    val bugEssenceFlowingLocation = ResourceLocation("bugmagic:textures/blocks/bug_essence_flow.png")

    fun createTextures() {
        val bugEssenceStill = TextureUtil.recolourTexture(waterStill, "bugmagic:" + bugEssenceStillLocation.resourcePath, "#BBFF70")
        val bugEssenceFlowing = TextureUtil.recolourTexture(waterFlow, "bugmagic:" + bugEssenceFlowingLocation.resourcePath, "#BBFF70")
    }
}