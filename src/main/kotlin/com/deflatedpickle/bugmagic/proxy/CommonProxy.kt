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
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side


open class CommonProxy {
    fun preInit(event: FMLPreInitializationEvent) {
        // BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.CLIENT)
        BugMagic.networkWrapper.registerMessage(PacketBugPower::class.java, PacketBugPower::class.java, 0, Side.SERVER)
        BugMagic.networkWrapper.registerMessage(PacketWand::class.java, PacketWand::class.java, 1, Side.SERVER)

        ModCreativeTabs
        ModItems
        ModBlocks
        ModTileEntities
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
        ModEntities

        // TODO: Move to an object
        AltarUtil.addRecipe(ModItems.spellFirefly,
                listOf(ModItems.partLeg, ModItems.partLeg, ModItems.partLeg, ModItems.partLeg,
                        ModItems.partWing, ModItems.partWing,
                        ModItems.partBody, ModItems.partHead,
                        Items.GLOWSTONE_DUST))
    }

    open fun getPlayer(): EntityPlayer? {
        return null
    }
}