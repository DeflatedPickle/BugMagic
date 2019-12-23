/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import net.minecraft.client.renderer.BufferBuilder

fun BufferBuilder.tex(u: Float, v: Float): BufferBuilder {
    return this.tex(u.toDouble(), v.toDouble())
}

fun BufferBuilder.tex(u: Double, v: Float): BufferBuilder {
    return this.tex(u, v.toDouble())
}

fun BufferBuilder.tex(u: Float, v: Double): BufferBuilder {
    return this.tex(u.toDouble(), v)
}
