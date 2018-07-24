package com.deflatedpickle.bugmagic.models

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler

object ModelFirefly {
    private val container = TabulaModelHandler.INSTANCE.loadTabulaModel("assets/bugmagic/models/entities/firefly.tbl")
    val model = TabulaModel(container)
}