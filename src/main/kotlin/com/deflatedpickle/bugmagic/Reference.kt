package com.deflatedpickle.bugmagic

object Reference {
    const val MOD_ID = "bugmagic"
    const val NAME = "BugMagic"
    // Versions follow this format: MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.
    const val VERSION = "1.12.2-0.0.0.0"
    const val ACCEPTED_VERSIONS = "[1.12.1, 1.12.2]"

    const val CLIENT_PROXY_CLASS = "com.deflatedpickle.bugmagic.client.Proxy"
    const val SERVER_PROXY_CLASS = "com.deflatedpickle.bugmagic.server.Proxy"

    const val DEPENDENCIES = "required-after:forgelin;required-after:glasspane;required-after:picklelib;required-after:modelloader"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
}