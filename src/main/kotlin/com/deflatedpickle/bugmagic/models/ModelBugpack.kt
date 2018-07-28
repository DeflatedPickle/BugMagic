package com.deflatedpickle.bugmagic.models

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler

object ModelBugpack {
    private val container = TabulaModelHandler.INSTANCE.loadTabulaModel("assets/bugmagic/models/entity/bugpack.tbl")
    val model = TabulaModel(container)
}