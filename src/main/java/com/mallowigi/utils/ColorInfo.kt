/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.mallowigi.utils

import com.mallowigi.colors.ColorsService.Companion.instance
import java.awt.Color
import kotlin.math.roundToInt

/**
 * Service storing many kinds of text format for a color
 *
 * @property color
 * @constructor Create empty Color info
 */
data class ColorInfo(val color: Color) {
  val txtRGB: String
  val txtHSB: String
  val txtHSL: String
  val shortHex: String
  val hex: String
  val decimalRGB: String
  val percentRGB: String
  val decimalRGBa: String
  val percentRGBa: String
  val hsl: String
  val hsla: String
  val colorFFF: String
  val colorFFFF: String
  val colorI: String
  val colorIB: String
  val colorIII: String
  val colorIIII: String
  val colorUIResourceFFF: String
  val colorUIResourceI: String
  val colorUIResourceIII: String

  init {
    // TODO: these should be methods, and replace the ones in the ColorUtils
    val argb = color.rgb
    val rgb = argb and 0xffffff
    val comps = color.getRGBComponents(null)
    val hslComps = ColorUtils.RGBtoHSL(color.red, color.green, color.blue, null)
    val hsbComps = Color.RGBtoHSB(color.red, color.green, color.blue, null)
    txtRGB = String.format("%d, %d, %d", color.red, color.green, color.blue)
    txtHSB = String.format("%d, %d%%, %d%%", toDegrees(hsbComps[0]), toPercent(hsbComps[1]), toPercent(hsbComps[2]))
    txtHSL = String.format("%d, %d%%, %d%%", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]))
    shortHex = String.format(if (isHexUppercase) "#%1X%1X%1X" else "#%1x%1x%1x", color.red / 16, color.green / 16,
      color.blue / 16)
    hex = String.format(if (isHexUppercase) "#%06X" else "#%06x", rgb)
    decimalRGB = String.format("rgb(%d, %d, %d)", color.red, color.green, color.blue)
    percentRGB = String.format("rgb(%d%%, %d%%, %d%%)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]))
    decimalRGBa = String.format("rgba(%d, %d, %d, %.2f)", color.red, color.green, color.blue, comps[3])
    percentRGBa = String.format("rgba(%d%%, %d%%, %d%%, %.2f)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]), comps[3])
    hsl = String.format("hsl(%d, %d%%, %d%%)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]))
    hsla = String.format("hsla(%d, %d%%, %d%%, %.2f)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]), comps[3])
    colorFFF = String.format("Color(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2])
    colorFFFF = String.format("Color(%.2ff, %.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2], comps[3])
    colorI = String.format(if (isHexUppercase) "Color(0x%06X)" else "Color(0x%06x)", rgb)
    colorIB = String.format(if (isHexUppercase) "Color(0x%08X, true)" else "Color(0x%08x, true)", argb)
    colorIII = String.format("Color(%d, %d, %d)", color.red, color.green, color.blue)
    colorIIII = String.format("Color(%d, %d, %d, %d)", color.red, color.green, color.blue, color.alpha)
    colorUIResourceFFF = String.format("ColorUIResource(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2])
    colorUIResourceI = String.format(if (isHexUppercase) "ColorUIResource(0x%06X)" else "ColorUIResource(0x%06x)", rgb)
    colorUIResourceIII = String.format("ColorUIResource(%d, %d, %d)", color.red, color.green, color.blue)
  }

  val sVGName: String?
    get() = instance.findSVGName(color)

  val javaName: String?
    get() = instance.findJavaName(color)

  companion object {
    var isHexUppercase: Boolean = false

    private fun toPercent(`val`: Float): Int = (`val` * 100).roundToInt()

    private fun toDegrees(`val`: Float): Int = (`val` * 359).roundToInt()
  }
}
