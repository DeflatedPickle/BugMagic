/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic

import com.cout970.modelloader.api.ModelLoaderApi
import com.deflatedpickle.bugmagic.api.common.tab.GenericTab
import com.deflatedpickle.bugmagic.common.CommonProxy
import com.deflatedpickle.bugmagic.common.command.CommandClearSpell
import com.deflatedpickle.bugmagic.common.command.CommandLearnSpell
import com.deflatedpickle.bugmagic.common.command.CommandSetEssence
import com.deflatedpickle.bugmagic.common.init.BlockInit
import com.deflatedpickle.bugmagic.common.init.EnchantmentInit
import com.deflatedpickle.bugmagic.common.init.EnchantmentTypeInit
import com.deflatedpickle.bugmagic.common.init.ItemInit
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
	modid = Reference.MOD_ID,
	name = Reference.NAME,
	version = Reference.VERSION,
	acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS,
	dependencies = Reference.DEPENDENCIES,
	modLanguageAdapter = Reference.ADAPTER
)
object BugMagic {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	var proxy: CommonProxy? = null

	val CHANNEL: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID)

	val logger: Logger = LogManager.getLogger(Reference.MOD_ID)

	val tab = GenericTab(
		name = "bugmagic",
		iconStack = ItemStack(Items.APPLE),
		hasSearchBar = true,
		relevantEnchantmentTypes = arrayOf(
			EnchantmentTypeInit.WAND
		)
	)

	init {
		FluidRegistry.enableUniversalBucket()
	}

	@EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		logger.info("Starting preInit.")
		ModelLoaderApi.registerDomain(Reference.MOD_ID)
		proxy!!.preInit(event)
		logger.info("Finished preInit.")
	}

	@EventHandler
	fun init(event: FMLInitializationEvent) {
		logger.info("Starting init.")
		proxy!!.init(event)
		logger.info("Finished init.")
	}

	@Mod.EventHandler
	fun serverStarting(event: FMLServerStartingEvent) {
		for (i in setOf(
			CommandLearnSpell(),
			CommandClearSpell(),
			CommandSetEssence()
		)) {
			event.registerServerCommand(i)
		}
	}
}
