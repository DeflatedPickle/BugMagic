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

        // Bugsics
        val categoryBugsics = LinkedHashMap<ResourceLocation, EntryAbstract>()

        val entryWelcome = ArrayList<IPage>()
        entryWelcome.add(PageText("This guide will serve as an introduction into the certain magic of bugs. Now, be weary, this magic is not for the simple wizard or witch, as, to progress any true amount, you will have to partake in a cult of bugs, and have to sacrifice bugs to create new spells."))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_welcome")] = EntryItemStack(entryWelcome, "bugmagic.buganomicon.bugsics.welcome.title", ItemStack(Items.BOOK))


        val entryWand = ArrayList<IPage>()
        entryWand.add(PageText("To get started in the vein of magic with so-called bugs, a wizard or witch must find or craft themselves the basic Wand of the Bugoneer. It can be crafted at a table with a combination of sticks formed in the pattern on the next page."))
        entryWand.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:generic_wand_recipe"),
                ItemStack(ModItems.wandGeneric),
                "  S",
                " S ",
                "S  ",
                'S', Items.STICK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_wand")] = EntryItemStack(entryWand, "bugmagic.buganomicon.bugsics.wand.title", ItemStack(ModItems.wandGeneric))

        val entryBugNet = ArrayList<IPage>()
        entryBugNet.add(PageText("The bug net is used to capture bugs. It can be crafted with the recipe on the next page."))
        entryBugNet.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_net_recipe"),
                ItemStack(ModItems.bugNet),
                " WS",
                " WS",
                "W  ",
                'W', Items.STICK, 'S', Items.STRING)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_net")] = EntryItemStack(entryBugNet, "bugmagic.buganomicon.bugsics.net.title", ItemStack(ModItems.bugNet))

        val entryMagnifyingGlass = ArrayList<IPage>()
        entryMagnifyingGlass.add(PageText("The magnifying glass is used to examine bugs inside of a bug jar, to obtain bug parts. It can be crafted with the recipe on the next page."))
        entryMagnifyingGlass.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:magnifying_glass_recipe"),
                ItemStack(ModItems.magnifyingGlass),
                "  G",
                " S ",
                "S  ",
                'G', Blocks.GLASS, 'S', Items.STICK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_magnifying")] = EntryItemStack(entryMagnifyingGlass, "bugmagic.buganomicon.bugsics.magnifying.title", ItemStack(ModItems.magnifyingGlass))

        val entryBugPart = ArrayList<IPage>()
        entryBugPart.add(PageText("Bug parts are used to form scrolls on the altar, they can be acquired by examining bugs inside jars.\n\nParts come in four forms; legs, wings, bodies and heads. These are required in different amounts at an altar to craft spells."))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_part")] = EntryItemStack(entryBugPart, "bugmagic.buganomicon.bugsics.part.title", ItemStack(ModItems.partLeg))

        val entryBugJar = ArrayList<IPage>()
        entryBugJar.add(PageText("The bug jar is used to hold bugs caught with the bug net. The bugs inside can be examined by using the magnifying glass. It can be crafted with the recipe on the next page."))
        entryBugJar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:bug_jar_recipe"),
                ItemStack(ModBlocks.bugJar),
                " W ",
                "G G",
                "GGG",
                'W', Blocks.PLANKS, 'G', Blocks.GLASS)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_jar")] = EntryItemStack(entryBugJar, "bugmagic.buganomicon.bugsics.jar.title", ItemStack(ModBlocks.bugJar))
        binder.addCategory(CategoryItemStack(categoryBugsics, "Bugsics", ItemStack(ModItems.wandGeneric)))
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