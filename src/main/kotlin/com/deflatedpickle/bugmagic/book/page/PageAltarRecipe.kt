package com.deflatedpickle.bugmagic.book.page

import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.Page
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.api.util.GuiHelper
import amerifrance.guideapi.gui.GuiBase
import amerifrance.guideapi.gui.GuiEntry
import com.deflatedpickle.bugmagic.init.ModBlocks
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PageAltarRecipe(private val result: Item, private val ingredients: List<Item>) : Page() {
    val requirements = mutableListOf<String>()

    @SideOnly(Side.CLIENT)
    override fun onInit(book: Book?, category: CategoryAbstract?, entry: EntryAbstract?, player: EntityPlayer?, bookStack: ItemStack?, guiEntry: GuiEntry?) {
        requirements.add("§fRequirements")
        for (i in ingredients.groupingBy { it }.eachCount()) {
            requirements.add("§7${i.value}x ${i.key.getItemStackDisplayName(ItemStack(i.key))}")
        }
    }

    @SideOnly(Side.CLIENT)
    override fun draw(book: Book, category: CategoryAbstract, entry: EntryAbstract, guiLeft: Int, guiTop: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, fontRendererObj: FontRenderer) {
        val centerX = guiLeft + 87
        val centerY = guiTop + 76
        GuiHelper.drawScaledItemStack(ItemStack(ModBlocks.altar), centerX - 7, centerY - 6, 2f)
        // GuiHelper.drawScaledItemStack(ItemStack(result), guiLeft + 75, guiTop + 115, 2.5f)

        // TODO: Add an arrow pointing clockwise from the first/gilded item
        // TODO: Split into an inner and outer circle for big recipes

        // Adapted from an answer on StackOverflow by trashgod
        // Link: https://stackoverflow.com/a/2510048
        val min = Math.min(centerX, centerY)
        val radius = 2.5 * min / 6

        val segments = ingredients.size
        val scale = 4

        var tooltip = mutableListOf<String>()

        for (i in 0 until segments) {
            val t = 2 * Math.PI * i / segments
            val x = Math.round(centerX + radius * Math.cos(t))
            val y = Math.round(centerY + radius * Math.sin(t))

            // Background
            Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("bugmagic:textures/gui/profile/item/backing.png"))
            GuiHelper.drawSizedIconWithoutColor(x.toInt() - 4, y.toInt() - 4, guiBase.xSize / scale, guiBase.ySize / scale, 0.0f)

            // Gilding
            if (i == 0) {
                Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("bugmagic:textures/gui/profile/item/gilding.png"))
                GuiHelper.drawSizedIconWithoutColor(x.toInt() - 4, y.toInt() - 4, guiBase.xSize / scale, guiBase.ySize / scale, 0.0f)
            }

            // Item
            GuiHelper.drawScaledItemStack(ItemStack(ingredients[i]), x.toInt(), y.toInt(), 1f)

            if (GuiHelper.isMouseBetween(mouseX, mouseY, x.toInt(), y.toInt(), 16,16)) {
                tooltip = GuiHelper.getTooltip(ItemStack(ingredients[i]))
            }
        }

        if (!tooltip.isEmpty()) {
            guiBase.drawHoveringText(tooltip, mouseX, mouseY)
            tooltip.clear()
        }

        if (GuiHelper.isMouseBetween(mouseX, mouseY, centerX - 4, centerY - 4, 24, 28)) {
            guiBase.drawHoveringText(requirements, mouseX, mouseY)
        }
    }
}