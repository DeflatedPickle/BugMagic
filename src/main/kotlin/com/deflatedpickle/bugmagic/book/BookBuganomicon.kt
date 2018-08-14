package com.deflatedpickle.bugmagic.book

import amerifrance.guideapi.api.GuideAPI
import amerifrance.guideapi.api.GuideBook
import amerifrance.guideapi.api.IGuideBook
import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.BookBinder
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
import amerifrance.guideapi.api.util.PageHelper
import java.util.ArrayList
import net.minecraft.init.Blocks
import amerifrance.guideapi.category.CategoryItemStack
import amerifrance.guideapi.page.PageIRecipe
import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.book.page.PageAltarRecipe
import com.deflatedpickle.bugmagic.book.page.PageSpellProfile
import com.deflatedpickle.bugmagic.init.ModBlocks
import com.deflatedpickle.bugmagic.proxy.ClientProxy
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
        val title = "bugmagic.buganomicon.title"
        binder.setGuideTitle(title)
        binder.setItemName(title)
        binder.setHeader(title)
        binder.setAuthor("DeflatedPickle")
        binder.setCreativeTab(ModCreativeTabs.tabGeneral)

        if (BugMagic.proxy is ClientProxy) {
            // Bugsics
            val categoryBugsics = LinkedHashMap<ResourceLocation, EntryAbstract>()

            val entryWelcome = ArrayList<IPage>()
            entryWelcome.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.welcome.description"), 300))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_welcome")] = EntryItemStack(entryWelcome, "bugmagic.buganomicon.bugsics.welcome.title", ItemStack(Items.BOOK))

            val entryWand = ArrayList<IPage>()
            entryWand.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.wand.description"), 300))
            entryWand.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:generic_wand_recipe"),
                    ItemStack(ModItems.wandGeneric),
                    "  S",
                    " S ",
                    "S  ",
                    'S', Items.STICK)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_wand")] = EntryItemStack(entryWand, "bugmagic.buganomicon.bugsics.wand.title", ItemStack(ModItems.wandGeneric))

            val entryBugNet = ArrayList<IPage>()
            entryBugNet.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.net.description"), 300))
            entryBugNet.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_net_recipe"),
                    ItemStack(ModItems.bugNet),
                    " WS",
                    " WS",
                    "W  ",
                    'W', Items.STICK, 'S', Items.STRING)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_net")] = EntryItemStack(entryBugNet, "bugmagic.buganomicon.bugsics.net.title", ItemStack(ModItems.bugNet))

            val entryMagnifyingGlass = ArrayList<IPage>()
            entryMagnifyingGlass.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.magnifying.description"), 300))
            entryMagnifyingGlass.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:magnifying_glass_recipe"),
                    ItemStack(ModItems.magnifyingGlass),
                    "  G",
                    " S ",
                    "S  ",
                    'G', Blocks.GLASS, 'S', Items.STICK)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_magnifying")] = EntryItemStack(entryMagnifyingGlass, "bugmagic.buganomicon.bugsics.magnifying.title", ItemStack(ModItems.magnifyingGlass))

            val entryBugPart = ArrayList<IPage>()
            entryBugPart.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.part.description"), 300))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_part")] = EntryItemStack(entryBugPart, "bugmagic.buganomicon.bugsics.part.title", ItemStack(ModItems.partLeg))

            val entryBugJar = ArrayList<IPage>()
            entryBugJar.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.jar.description"), 300))
            entryBugJar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_jar_recipe"),
                    ItemStack(ModBlocks.bugJar),
                    " W ",
                    "G G",
                    "GGG",
                    'W', Blocks.PLANKS, 'G', Blocks.GLASS)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_jar")] = EntryItemStack(entryBugJar, "bugmagic.buganomicon.bugsics.jar.title", ItemStack(ModBlocks.bugJar))

            val entryCauldron = ArrayList<IPage>()
            entryCauldron.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.cauldron.description"), 300))
            entryCauldron.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:cauldron_recipe"),
                    ItemStack(ModBlocks.cauldron),
                    "I I",
                    "I I",
                    "IBI",
                    'I', Items.IRON_INGOT, 'B', Blocks.IRON_BLOCK)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_cauldron")] = EntryItemStack(entryCauldron, "bugmagic.buganomicon.bugsics.cauldron.title", ItemStack(ModBlocks.cauldron))

            val entryAltar = ArrayList<IPage>()
            entryAltar.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.altar.description"), 300))
            entryAltar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:altar_recipe"),
                    ItemStack(ModBlocks.altar),
                    "SSS",
                    " W ",
                    "SSS",
                    'S', Blocks.STONE_SLAB, 'W', Blocks.PLANKS)))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_altar")] = EntryItemStack(entryAltar, "bugmagic.buganomicon.bugsics.altar.title", ItemStack(ModBlocks.altar))

            val entrySpell = ArrayList<IPage>()
            entrySpell.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugsics.spell.description"), 300))
            categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_spell")] = EntryItemStack(entrySpell, "bugmagic.buganomicon.bugsics.spell.title", ItemStack(ModItems.spellFirefly))

            binder.addCategory(CategoryItemStack(categoryBugsics, "bugmagic.buganomicon.bugsics.title", ItemStack(ModItems.wandGeneric)))

            // Bugstiary
            val categoryBugstiary = LinkedHashMap<ResourceLocation, EntryAbstract>()

            val entryFirefly = ArrayList<IPage>()
            // TODO: Write a custom image class to render a border and a smaller image
            entryFirefly.add(PageSpellProfile(SpellFirefly(), ResourceLocation("bugmagic:textures/gui/spells/firefly.png")))
            entryFirefly.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugstiary.firefly.description"), 300))
            entryFirefly.add(PageAltarRecipe(ModItems.spellFirefly, BugMagic.proxy!!.fireflyList))
            categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_firefly")] = EntryItemStack(entryFirefly, "bugmagic.buganomicon.bugstiary.firefly.title", ItemStack(ModItems.spellFirefly))

            val entryBugpack = ArrayList<IPage>()
            entryBugpack.add(PageSpellProfile(SpellBugpack(), ResourceLocation("bugmagic:textures/gui/spells/bugpack.png")))
            entryBugpack.addAll(PageHelper.pagesForLongText(I18n.format("bugmagic.buganomicon.bugstiary.bugpack.description"), 300))
            entryBugpack.add(PageAltarRecipe(ModItems.spellBugpack, BugMagic.proxy!!.bugpackList))
            categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_bugpack")] = EntryItemStack(entryBugpack, "bugmagic.buganomicon.bugstiary.bugpack.title", ItemStack(ModItems.spellBugpack))

            binder.addCategory(CategoryItemStack(categoryBugstiary, "bugmagic.buganomicon.bugstiary.title", ItemStack(ModItems.partHead)))
        }

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