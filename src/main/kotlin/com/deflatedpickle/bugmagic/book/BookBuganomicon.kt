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
        val title = "bugmagic.buganomicon.title"
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

        val entryCauldron = ArrayList<IPage>()
        entryCauldron.add(PageText("The cauldron is used to boil bug parts into bug resonance, which can be bottled up and drunk to regenerate bug power. It can be crafted with the recipe on the next page."))
        entryCauldron.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:cauldron_recipe"),
                ItemStack(ModBlocks.cauldron),
                "I I",
                "I I",
                "IBI",
                'I', Items.IRON_INGOT, 'B', Blocks.IRON_BLOCK)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_cauldron")] = EntryItemStack(entryCauldron, "bugmagic.buganomicon.bugsics.cauldron.title", ItemStack(ModBlocks.cauldron))

        val entryAltar = ArrayList<IPage>()
        entryAltar.add(PageText("The altar is used to form spells using bug parts and other items. The recipes in it must follow a specific order that will be shown in the spell pages. It can be crafted with the recipe on the next page."))
        entryAltar.add(PageIRecipe(ShapedOreRecipe(ResourceLocation("bugmagic:altar_recipe"),
                ItemStack(ModBlocks.altar),
                "SSS",
                " W ",
                "SSS",
                'S', Blocks.STONE_SLAB, 'W', Blocks.PLANKS)))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_altar")] = EntryItemStack(entryAltar, "bugmagic.buganomicon.bugsics.altar.title", ItemStack(ModBlocks.altar))

        val entrySpell = ArrayList<IPage>()
        entrySpell.add(PageText("Spells are acquired in the form of scrolls, found in chests, crafted or given by your cult leader (if you decided to join one). The scroll can be learnt to gain the spell and then selected with your wand. Each spell has a different class, cost, drain cost, drain interval, cast limit and cooldown."))
        entrySpell.add(PageText("The cost is simply how much bug power it will cost to cast the spell, the drain cost is how much bug power will be drained every drain interval whilst the spell is cast, the drain interval is how long it will take to drain, the cast limit is how many of the spell can be cast at once, and the cooldown is how long"))
        entrySpell.add(PageText("it will take for the wand to cooldown after casting the spell."))
        categoryBugsics[ResourceLocation(Reference.MOD_ID, "page_spell")] = EntryItemStack(entrySpell, "bugmagic.buganomicon.bugsics.spell.title", ItemStack(ModItems.spellFirefly))

        binder.addCategory(CategoryItemStack(categoryBugsics, "bugmagic.buganomicon.bugsics.title", ItemStack(ModItems.wandGeneric)))

        // Bugstiary
        val categoryBugstiary = LinkedHashMap<ResourceLocation, EntryAbstract>()

        val entryFirefly = ArrayList<IPage>()
        // TODO: Write a custom image class to render a border and a smaller image
        entryFirefly.add(PageSpellProfile(SpellFirefly(), ResourceLocation("bugmagic:textures/gui/spells/firefly.png")))
        entryFirefly.add(PageText("The Firefly spell will spawn a small fly that will follow you, hovering in the air, that will light up the area just around it.\nThough the Firefly is not at all smart, and can get caught on blocks or even kill itself."))
        entryFirefly.add(PageAltarRecipe(ModItems.spellFirefly, BugMagic.proxy!!.fireflyList))
        categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_firefly")] = EntryItemStack(entryFirefly, "bugmagic.buganomicon.bugsics.firefly.title", ItemStack(ModItems.spellFirefly))

        val entryBugpack = ArrayList<IPage>()
        entryBugpack.add(PageSpellProfile(SpellBugpack(), ResourceLocation("bugmagic:textures/gui/spells/bugpack.png")))
        entryBugpack.add(PageText("The Bugpack spell will spawn a small fly that will follow you, hovering in the air, and will hold a single stack for you.\nThough the Bugpack is not at all smart, and can get caught on blocks or even kill itself."))
        entryBugpack.add(PageAltarRecipe(ModItems.spellBugpack, BugMagic.proxy!!.bugpackList))
        categoryBugstiary[ResourceLocation(Reference.MOD_ID, "page_bugpack")] = EntryItemStack(entryBugpack, "bugmagic.buganomicon.bugsics.bugpack.title", ItemStack(ModItems.spellBugpack))

        binder.addCategory(CategoryItemStack(categoryBugstiary, "bugmagic.buganomicon.bugstiary.title", ItemStack(ModItems.partHead)))

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