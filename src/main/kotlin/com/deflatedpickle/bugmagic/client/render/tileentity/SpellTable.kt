/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.render.tileentity

import com.deflatedpickle.bugmagic.api.client.util.extension.drawNameTag
import com.deflatedpickle.bugmagic.api.client.util.extension.render
import com.deflatedpickle.bugmagic.api.client.util.extension.renderCube
import com.deflatedpickle.bugmagic.api.common.util.Math
import com.deflatedpickle.bugmagic.common.block.tileentity.SpellTable as SpellTableTE
import com.deflatedpickle.bugmagic.common.item.Wand
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class SpellTable : TileEntitySpecialRenderer<SpellTableTE>() {

    val eyeStack = ItemStack(Items.SPIDER_EYE)
    val paperStack = ItemStack(Items.PAPER)

    override fun render(te: SpellTableTE, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 140f, 140f)
        RenderHelper.enableStandardItemLighting()

        // Progress
        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, 0.0f, 0f)
        Minecraft.getMinecraft().fontRenderer.drawNameTag("${te.recipeProgression}", x.toInt(), y.toInt())
        GlStateManager.popMatrix()

        if (te.validRecipe != SpellTableTE.invalidRecipe) {
        }

        // Fluid
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 1.135, y + 0.515, z + 0.57)

        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, 0.2f, 0f)
        Minecraft.getMinecraft().fontRenderer.drawNameTag("${te.fluidTank.fluid?.localizedName
                ?: "Empty"}\n${te.fluidTank.fluidAmount} / 1000 mB", x.toInt(), y.toInt())
        GlStateManager.popMatrix()

        GlStateManager.rotate(22.5f, 0f, 0f, 1f)

        te.fluidTank.fluid?.fluid?.still?.renderCube(0.1, (0.4 * (te.fluidTank.fluidAmount / 1000)), 0.18)

        GlStateManager.popMatrix()

        // Wand
        val wandStack = te.wandStackHandler.getStackInSlot(0)
        if (wandStack.item is Wand && wandStack.count > 0) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x + 1.1, y + 0.6, z + 0.3)

            Minecraft.getMinecraft().fontRenderer.drawNameTag(wandStack.displayName, x.toInt(), y.toInt())

            GlStateManager.rotate(60f, 0.2f, -0.8f, 1f)

            wandStack.render(world)

            GlStateManager.popMatrix()
        }

        // Feather
        val featherStack = te.featherStackHandler.getStackInSlot(0)
        if (featherStack.item == Items.FEATHER && featherStack.count > 0) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x + 0.18, y + 0.92, z + 0.7)
            GlStateManager.rotate(70f, 0f, 0.4f, 1f)
            GlStateManager.scale(0.7f, 0.7f, 0.7f)

            featherStack.render(world)

            GlStateManager.popMatrix()
        }

        // Ink
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.11, y + 0.82, z + 0.6)

        Minecraft.getMinecraft().fontRenderer.drawNameTag("${te.ink} / 1.0", x.toInt(), y.toInt())

        GlStateManager.rotate(-22.5f, 0f, 1f, 0f)

        ResourceLocation("minecraft:blocks/water_still").renderCube(0.18, 0.08 * te.ink, 0.18)

        GlStateManager.popMatrix()

        // Eye
        // TODO: Make the eye look at the player's eyes
        GlStateManager.pushMatrix()
        val eyeSpeed = 0.02
        val eyeAmplitude = 0.02
        GlStateManager.translate(x + 0.8, y + 0.15 + sin(te.world.totalWorldTime.toFloat() * eyeSpeed) * eyeAmplitude, z + 0.45)
        GlStateManager.rotate(90f, 0f, 1f, 0f)
        GlStateManager.rotate(180f - Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GlStateManager.scale(0.6f, 0.6f, 0.6f)

        this.eyeStack.render(world)

        GlStateManager.popMatrix()

        // Valid render
        if (te.validRecipe != SpellTableTE.invalidRecipe) {
            GlStateManager.pushMatrix()

            val validSpeed = 0.06f
            val validAmplitude = 0.02f
            GlStateManager.translate(x + 0.5, y + 1.5 + sin(te.world.totalWorldTime.toFloat() * validSpeed) * validAmplitude, z + 0.4)
            GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 0.2f, 0f)

            paperStack.render(world)

            GlStateManager.popMatrix()
        }

        // Item ring
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1, z + 0.5)

        val itemCount = 0.until(te.itemStackHandler.slots).count { te.itemStackHandler.getStackInSlot(it) != ItemStack.EMPTY }

        GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 0.2f, 0f)
        GlStateManager.pushMatrix()

        for (i in 0 until te.itemStackHandler.slots) {
            val stack = te.itemStackHandler.getStackInSlot(i)

            // Renders all non-empty stacks in a circle
            if (stack != ItemStack.EMPTY) {
                GlStateManager.pushMatrix()

                val bigTheta = (PI * 2) * i / itemCount

                val bigRadius = 1.2
                val bigX = bigRadius * cos(bigTheta)
                val bigZ = bigRadius * sin(bigTheta)

                val bigSpeed = 0.1f
                val bigAmplitude = 0.06
                GlStateManager.translate(bigX, sin(te.world.totalWorldTime.toFloat() * bigSpeed) * bigAmplitude, bigZ)

                GlStateManager.pushMatrix()
                GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, -0.4f, 0f)
                Minecraft.getMinecraft().fontRenderer.drawNameTag("${stack.displayName} x${stack.count}", x.toInt(), y.toInt())
                GlStateManager.popMatrix()

                GlStateManager.rotate(te.world.totalWorldTime.toFloat(), 0f, 1.2f, 0f)

                // Renders all items in the stack in a smaller circle
                for (j in 1..stack.count) {
                    GlStateManager.pushMatrix()

                    val smallTheta = (PI * 2) * j / stack.count

                    val smallRadius = 0.4
                    val smallX = smallRadius * cos(smallTheta)
                    val smallZ = smallRadius * sin(smallTheta)

                    val smallSpeed = 0.03
                    val smallAmplitude = 0.08f
                    GlStateManager.translate(smallX, sin(((te.world.totalWorldTime + 360 /
                            // If J is less than half of the item stack, uses J
                            // If J is more, J is reversed with the stack size
                            if (j < stack.count / 2) j else Math.reverse(1, stack.count, j)
                            // Pi * 2 is a bit more bumpy and less of a connected loop
                            // Pi * 2.55 seems to be a sweet spot
                            ) * (PI * 2.55)) * smallSpeed) * smallAmplitude, smallZ)

                    stack.render(world)

                    GlStateManager.popMatrix()
                }

                GlStateManager.popMatrix()
            }
        }

        GlStateManager.popMatrix()
        GlStateManager.popMatrix()

        RenderHelper.disableStandardItemLighting()
    }
}
