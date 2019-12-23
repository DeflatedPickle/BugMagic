/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util

object Math {
    // https://en.wikipedia.org/wiki/Linear_interpolation
    fun lerp(first: Float, second: Float, time: Float): Float = (1 - time) * first + time * second

    // https://stackoverflow.com/a/16242301
    fun reverse(min: Int, max: Int, value: Int) = (max + min) - value
}
