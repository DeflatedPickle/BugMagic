/* Copyright (c) 2020 DeflatedPickle under the MIT license */

@file:Suppress("unused")

package com.deflatedpickle.bugmagic.api.common.util.function

import net.minecraft.util.text.TextFormatting

fun join(vararg strings: String) = strings.joinToString(" | ")
fun format(string: String, formatting: TextFormatting): String = "$formatting$string$reset"

operator fun TextFormatting.minus(string: String): String = format(string, this)
operator fun TextFormatting.minus(int: Int): String = this - int.toString()
operator fun TextFormatting.minus(int: Float): String = this - int.toString()
operator fun TextFormatting.minus(int: Double): String = this - int.toString()

val black = TextFormatting.BLACK
val darkBlue = TextFormatting.DARK_BLUE
val darkGreen = TextFormatting.DARK_GREEN
val darkAqua = TextFormatting.DARK_AQUA
val darkRed = TextFormatting.DARK_RED
val darkPurple = TextFormatting.DARK_PURPLE
val gold = TextFormatting.GOLD
val gray = TextFormatting.GRAY
val darkGray = TextFormatting.DARK_GRAY
val blue = TextFormatting.BLUE
val green = TextFormatting.GREEN
val aqua = TextFormatting.AQUA
val red = TextFormatting.RED
val lightPurple = TextFormatting.LIGHT_PURPLE
val yellow = TextFormatting.YELLOW
val white = TextFormatting.WHITE
val obfuscated = TextFormatting.OBFUSCATED
val bold = TextFormatting.BOLD
val strikethrough = TextFormatting.STRIKETHROUGH
val underline = TextFormatting.UNDERLINE
val italic = TextFormatting.ITALIC
val reset = TextFormatting.RESET
