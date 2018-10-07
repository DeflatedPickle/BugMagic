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
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

open class CommonProxy {
    val fireflyList = listOf(ModItems.spellEmpty, ModItems.partLeg, ModItems.partLeg, ModItems.partLeg, ModItems.partLeg,
            ModItems.partWing, ModItems.partWing,
            ModItems.partBody, ModItems.partHead,
            Items.GLOWSTONE_DUST)

    val bugpackList = listOf(ModItems.spellEmpty, ModItems.partLeg, ModItems.partLeg, ModItems.partLeg, ModItems.partLeg,
            ModItems.partWing, ModItems.partWing,
            ModItems.partBody, ModItems.partHead,
            ItemStack(Blocks.CHEST).item)

    val wurmList = listOf(ModItems.spellEmpty, ModItems.partBody, ModItems.partHead,
            Items.BONE, ItemStack(Blocks.DIRT).item, ItemStack(Blocks.DIRT).item, ItemStack(Blocks.DIRT).item)

    fun preInit(event: FMLPreInitializationEvent) {
        // BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.CLIENT)
        BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.SERVER)
        BugMagic.networkWrapper.registerMessage(PacketWand::class.java, PacketWand::class.java, 1, Side.SERVER)

        ModTextures

        ModCreativeTabs
        ModItems
        ModBlocks
        ModTileEntities
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
        ModEntities
        ModCrafting

        // TODO: Move to an object
        AltarUtil.addRecipe(ModItems.spellFirefly, fireflyList)
        AltarUtil.addRecipe(ModItems.spellBugpack, bugpackList)
        AltarUtil.addRecipe(ModItems.spellWurm, wurmList)
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}