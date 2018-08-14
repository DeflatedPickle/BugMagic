package com.deflatedpickle.bugmagic.book.page

import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.Page
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.api.util.GuiHelper
import amerifrance.guideapi.gui.GuiBase
import amerifrance.guideapi.page.PageText
import com.deflatedpickle.bugmagic.spells.SpellBase
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PageSpellProfile(spell: SpellBase, val image: ResourceLocation) : Page() {
    private val pageText = PageText("Spell Class: ${spell.cult.capitalize()}\nCost: ${spell.cost}bp\nDrain Cost: ${spell.drain}bp\nDrain Interval: ${spell.drainWait / 20}s\nCast Limit: ${spell.castLimit}\nCooldown: ${spell.cooldownTime / 20}s")

    @SideOnly(Side.CLIENT)
    override fun draw(book: Book?, category: CategoryAbstract?, entry: EntryAbstract?, guiLeft: Int, guiTop: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase?, fontRendererObj: FontRenderer?) {
        // Image
        Minecraft.getMinecraft().textureManager.bindTexture(this.image)
        GuiHelper.drawSizedIconWithoutColor(guiLeft + 70, guiTop + 24, guiBase!!.xSize / 2, guiBase.ySize / 2, 0.0f)

        // Border
        Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("bugmagic:textures/gui/profile/border.png"))
        GuiHelper.drawSizedIconWithoutColor(guiLeft + 64, guiTop + 16, (guiBase.xSize / 2) + 30, (guiBase.ySize / 2) + 30, 0.0f)

        // Tape
        Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("bugmagic:textures/gui/profile/tape.png"))
        GuiHelper.drawSizedIconWithoutColor(guiLeft + 52, guiTop + 3, (guiBase.xSize / 2) + 80, (guiBase.ySize / 2) + 80, 0.0f)

        // Text
        pageText.setUnicodeFlag(this.unicode)
        pageText.draw(book, category, entry, guiLeft + 5, guiTop + 78, mouseX, mouseY, guiBase, fontRendererObj)
    }
}