package com.deflatedpickle.bugmagic

object Reference {
    const val MOD_ID = "bugmagic"
    const val NAME = "BugMagic"
    // Versions follow this format: MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.
    const val VERSION = "1.12.2-1.0.0.0"
    const val ACCEPTED_VERSIONS = "[1.12.1, 1.12.2]"

    const val CLIENT_PROXY_CLASS = "com.deflatedpickle.bugmagic.proxy.ClientProxy"
    const val SERVER_PROXY_CLASS = "com.deflatedpickle.bugmagic.proxy.ServerProxy"

    const val DEPENDENCIES = "required-after:forgelin"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
}