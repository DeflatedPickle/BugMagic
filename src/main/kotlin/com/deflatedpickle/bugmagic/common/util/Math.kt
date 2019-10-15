package com.deflatedpickle.bugmagic.common.util

object Math {
    // https://en.wikipedia.org/wiki/Linear_interpolation
    fun lerp(first: Float, second: Float, time: Float): Float {
        return (1 - time) * first + time * second
    }
}