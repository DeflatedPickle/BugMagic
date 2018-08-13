package com.deflatedpickle.bugmagic.book

import amerifrance.guideapi.api.GuideAPI
import amerifrance.guideapi.api.GuideBook
import amerifrance.guideapi.api.IGuideBook
import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.BookBinder
import amerifrance.guideapi.api.impl.Entry
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.entry.EntryItemStack
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import net.minecraft.client.resources.I18n
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.util.PageHelper
import java.util.ArrayList
import net.minecraft.init.Blocks
import amerifrance.guideapi.category.CategoryItemStack
import amerifrance.guideapi.entry.EntryResourceLocation
import amerifrance.guideapi.page.PageIRecipe
import amerifrance.guideapi.page.PageText
import amerifrance.guideapi.page.PageTextImage
import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.book.page.PageAltarRecipe
import com.deflatedpickle.bugmagic.book.page.PageSpellProfile
import com.deflatedpickle.bugmagic.init.ModBlocks
import com.deflatedpickle.bugmagic.spells.SpellBugpack
import com.deflatedpickle.bugmagic.spells.SpellFirefly
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.oredict.ShapedOreRecipe


@GuideBook
class BookBuganomicon : IGuideBook {
    lateinit var guide: Book

    override fun buildBook(): Book? {
        val binder = BookBinder(ResourceLocation(Reference.MOD_ID, "buganomicon"))

        // Setup
        val title = "Buganomicon"
        binder.setGuideTitle(title)
        binder.setItemName(title)
        binder.setHeader(title)
        binder.setAuthor("DeflatedPickle")
        binder.setCreativeTab(ModCreativeTabs.tabGeneral)

        binder.setSpawnWithBook()

        guide = binder.build()
        return guide
    }

    @SideOnly(Side.CLIENT)
    override fun handleModel(bookStack: ItemStack) {
        GuideAPI.setModel(guide)
    }

    override fun handlePost(bookStack: ItemStack) {
        // TODO: Move bug parts to a single item with variants
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.partLeg))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.partWing))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.partBody))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.partHead))
    }
}