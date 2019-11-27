package com.deflatedpickle.bugmagic

import com.cout970.modelloader.api.ModelLoaderApi
import com.deflatedpickle.bugmagic.common.Proxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
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
    var proxy: Proxy? = null

    val CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID)

    val logger: Logger = LogManager.getLogger(Reference.MOD_ID)

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
}