package com.deflatedpickle.bugmagic.models

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler

object ModelWurm {
    private val container = TabulaModelHandler.INSTANCE.loadTabulaModel("assets/bugmagic/models/entity/wurm.tbl")
    val model = TabulaModel(container)
}