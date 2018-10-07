package com.deflatedpickle.bugmagic.book

import amerifrance.guideapi.api.GuideAPI
import amerifrance.guideapi.api.GuideBook
import amerifrance.guideapi.api.IGuideBook
import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.BookBinder
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.api.util.PageHelper
import amerifrance.guideapi.category.CategoryItemStack
import amerifrance.guideapi.entry.EntryItemStack
import amerifrance.guideapi.page.PageIRecipe
import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.book.page.PageAltarRecipe
import com.deflatedpickle.bugmagic.book.page.PageSpellProfile
import com.deflatedpickle.bugmagic.init.ModBlocks
import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.spells.SpellBugpack
import com.deflatedpickle.bugmagic.spells.SpellFirefly
import com.deflatedpickle.bugmagic.spells.SpellWurm
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.ShapedOreRecipe
import java.util.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.set


@GuideBook
class BookBuganomicon : IGuideBook {
    private lateinit var guide: Book

    override fun buildBook(): Book? {
        val binder = BookBinder(ResourceLocation(Reference.MOD_ID, "buganomicon"))

        // Setup
        val title = "bugmagic.buganomicon.title"
        binder.setGuideTitle(title)
        binder.setItemName(title)
        binder.setHeader(title)
        binder.setAuthor("DeflatedPickle")
        binder.setCreativeTab(ModCreativeTabs.tabGeneral)

        // Bugsics
        val categoryBugsics = LinkedHashMap<ResourceLocation, EntryAbstract>()

        val entryWelcome = ArrayList<IPage>()
        entryWelcome.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.welcome.description"), 300))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_welcome")] = EntryItemStack(entryWelcome, "bugmagic.buganomicon.bugsics.welcome.title", ItemStack(Items.BOOK))

        // val entryBook = ArrayList<IPage>()
        // categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_book")] = EntryResourceLocation(entryBook, "bugmagic.buganomicon.bugsics.book.title", ResourceLocation("minecraft:textures/items/knowledge_book.png"))

        val entryWand = ArrayList<IPage>()
        entryWand.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.wand.description"), 300))
        entryWand.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:generic_wand_recipe"),
                ItemStack(ModItems.WAND_GENERIC),
                "  S",
                " S ",
                "S  ",
                'S', Items.STICK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_wand")] = EntryItemStack(entryWand, "bugmagic.buganomicon.bugsics.wand.title", ItemStack(ModItems.WAND_GENERIC))

        val entryBugNet = ArrayList<IPage>()
        entryBugNet.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.net.description"), 300))
        entryBugNet.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_net_recipe"),
                ItemStack(ModItems.BUG_NET),
                " WS",
                " WS",
                "W  ",
                'W', Items.STICK, 'S', Items.STRING)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_net")] = EntryItemStack(entryBugNet, "bugmagic.buganomicon.bugsics.net.title", ItemStack(ModItems.BUG_NET))

        val entryMagnifyingGlass = ArrayList<IPage>()
        entryMagnifyingGlass.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.magnifying.description"), 300))
        entryMagnifyingGlass.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:magnifying_glass_recipe"),
                ItemStack(ModItems.MAGNIFYING_GLASS),
                "  G",
                " S ",
                "S  ",
                'G', Blocks.GLASS, 'S', Items.STICK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_magnifying")] = EntryItemStack(entryMagnifyingGlass, "bugmagic.buganomicon.bugsics.magnifying.title", ItemStack(ModItems.MAGNIFYING_GLASS))

        val entryBugPart = ArrayList<IPage>()
        entryBugPart.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.part.description"), 300))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_part")] = EntryItemStack(entryBugPart, "bugmagic.buganomicon.bugsics.part.title", ItemStack(ModItems.BUG_PART_LEG))

        val entryBugJar = ArrayList<IPage>()
        entryBugJar.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.jar.description"), 300))
        entryBugJar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_jar_recipe"),
                ItemStack(ModBlocks.BUG_JAR),
                " W ",
                "G G",
                "GGG",
                'W', Blocks.PLANKS, 'G', Blocks.GLASS)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_jar")] = EntryItemStack(entryBugJar, "bugmagic.buganomicon.bugsics.jar.title", ItemStack(ModBlocks.BUG_JAR))

        val entryCauldron = ArrayList<IPage>()
        entryCauldron.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.cauldron.description"), 300))
        entryCauldron.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:cauldron_recipe"),
                ItemStack(ModBlocks.CAULDRON),
                "I I",
                "I I",
                "IBI",
                'I', Items.IRON_INGOT, 'B', Blocks.IRON_BLOCK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_cauldron")] = EntryItemStack(entryCauldron, "bugmagic.buganomicon.bugsics.cauldron.title", ItemStack(ModBlocks.CAULDRON))

        val entryAltar = ArrayList<IPage>()
        entryAltar.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.altar.description"), 300))
        entryAltar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:altar_recipe"),
                ItemStack(ModBlocks.ALTAR),
                "SSS",
                " W ",
                "SSS",
                'S', Blocks.STONE_SLAB, 'W', Blocks.PLANKS)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_altar")] = EntryItemStack(entryAltar, "bugmagic.buganomicon.bugsics.altar.title", ItemStack(ModBlocks.ALTAR))

        val entrySpell = ArrayList<IPage>()
        entrySpell.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugsics.spell.description"), 300))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_spell")] = EntryItemStack(entrySpell, "bugmagic.buganomicon.bugsics.spell.title", ItemStack(ModItems.SPELL_FIREFLY))

        binder.addCategory(CategoryItemStack(categoryBugsics, "bugmagic.buganomicon.bugsics.title", ItemStack(ModItems.WAND_GENERIC)))

        // Bugstiary
        val categoryBugstiary = LinkedHashMap<ResourceLocation, EntryAbstract>()

        val entryFirefly = ArrayList<IPage>()
        // TODO: Write a custom image class to render a border and a smaller image
        entryFirefly.add(PageSpellProfile(SpellFirefly(), ResourceLocation("bugmagic:textures/gui/spells/firefly.png")))
        entryFirefly.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugstiary.firefly.description"), 300))
        entryFirefly.add(PageAltarRecipe(ModItems.SPELL_FIREFLY, BugMagic.proxy.fireflyList))
        categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_firefly")] = EntryItemStack(entryFirefly, "bugmagic.buganomicon.bugstiary.firefly.title", ItemStack(ModItems.SPELL_FIREFLY))

        val entryBugpack = ArrayList<IPage>()
        entryBugpack.add(PageSpellProfile(SpellBugpack(), ResourceLocation("bugmagic:textures/gui/spells/bugpack.png")))
        entryBugpack.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugstiary.bugpack.description"), 300))
        entryBugpack.add(PageAltarRecipe(ModItems.SPELL_BUGPACK, BugMagic.proxy.bugpackList))
        categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_bugpack")] = EntryItemStack(entryBugpack, "bugmagic.buganomicon.bugstiary.bugpack.title", ItemStack(ModItems.SPELL_BUGPACK))

        val entryWurm = ArrayList<IPage>()
        entryWurm.add(PageSpellProfile(SpellWurm(), ResourceLocation("bugmagic:textures/gui/spells/wurm.png")))
        entryWurm.addAll(PageHelper.pagesForLongText(I18n.translateToLocal("bugmagic.buganomicon.bugstiary.wurm.description"), 300))
        entryWurm.add(PageAltarRecipe(ModItems.SPELL_WURM, BugMagic.proxy.wurmList))
        categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_wurm")] = EntryItemStack(entryWurm, "bugmagic.buganomicon.bugstiary.wurm.title", ItemStack(ModItems.SPELL_WURM))

        binder.addCategory(CategoryItemStack(categoryBugstiary, "bugmagic.buganomicon.bugstiary.title", ItemStack(ModItems.BUG_PART_HEAD)))

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
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.BUG_PART_LEG))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.BUG_PART_WING))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.BUG_PART_BODY))
        GameRegistry.addShapelessRecipe(ResourceLocation("guideapi:buganomicon"), ResourceLocation("bugmagic"), bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(ModItems.BUG_PART_HEAD))
    }
}