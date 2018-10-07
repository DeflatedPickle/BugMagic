package com.deflatedpickle.bugmagic.proxy

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.events.ForgeEventHandler
import com.deflatedpickle.bugmagic.init.*
import com.deflatedpickle.bugmagic.network.PacketBugPower
import com.deflatedpickle.bugmagic.network.PacketWand
import com.deflatedpickle.bugmagic.util.AltarUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.relauncher.Side

open class CommonProxy {
    val fireflyList = listOf(ModItems.SPELL_EMPTY, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG,
            ModItems.BUG_PART_WING, ModItems.BUG_PART_WING,
            ModItems.BUG_PART_BODY, ModItems.BUG_PART_HEAD,
            Items.GLOWSTONE_DUST)

    val bugpackList = listOf(ModItems.SPELL_EMPTY, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG, ModItems.BUG_PART_LEG,
            ModItems.BUG_PART_WING, ModItems.BUG_PART_WING,
            ModItems.BUG_PART_BODY, ModItems.BUG_PART_HEAD,
            ItemStack(Blocks.CHEST).item)

    val wurmList = listOf(ModItems.SPELL_EMPTY, ModItems.BUG_PART_BODY, ModItems.BUG_PART_HEAD,
            Items.BONE, ItemStack(Blocks.DIRT).item, ItemStack(Blocks.DIRT).item, ItemStack(Blocks.DIRT).item)

    open fun preInit() {
        // BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.CLIENT)
        BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.SERVER)
        BugMagic.networkWrapper.registerMessage(PacketWand::class.java, PacketWand::class.java, 1, Side.SERVER)

        ModCreativeTabs
        ModItems
        ModBlocks
        ModTileEntities
    }

    open fun init() {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
        ModEntities
        ModCrafting

        // TODO: Move to an object
        AltarUtil.addRecipe(ModItems.SPELL_FIREFLY, fireflyList)
        AltarUtil.addRecipe(ModItems.SPELL_BUGPACK, bugpackList)
        AltarUtil.addRecipe(ModItems.SPELL_WURM, wurmList)
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}