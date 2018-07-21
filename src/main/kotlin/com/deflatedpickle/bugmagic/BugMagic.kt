package com.deflatedpickle.bugmagic

import com.deflatedpickle.bugmagic.proxy.CommonProxy
import com.deflatedpickle.picklelib.PickleLib
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies = Reference.DEPENDENCIES, modLanguageAdapter = Reference.ADAPTER)
object BugMagic {
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    var proxy: CommonProxy? = null

    val networkWrapper: SimpleNetworkWrapper = SimpleNetworkWrapper(Reference.NAME)

    val logger: Logger = LogManager.getLogger(Reference.MOD_ID)

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger.info("Starting preInit.")
        PickleLib.setNameSpace(Reference.MOD_ID)
        proxy!!.preInit(event)
        logger.info("Finished preInit.")
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        logger.info("Starting init.")
        proxy!!.init(event)
        logger.info("Finished init.")
    }
}