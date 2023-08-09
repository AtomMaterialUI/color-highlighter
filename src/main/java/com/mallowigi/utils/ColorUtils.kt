/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2022 Elior "Mallowigi" Boukhobza
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
@file:Suppress("unused")

package com.mallowigi.utils

import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.ColorUtil
import com.intellij.ui.Gray
import com.mallowigi.config.home.ColorHighlighterConfig
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong

/** Color Utils! */
@Suppress("Detekt:MagicNumber")
object ColorUtils {

  private const val HASH: Char = '#'
  private const val CLOSE_PAREN: Char = ')'
  private const val COMMA: String = ","
  private const val OPEN_PAREN: Char = '('
  private const val PERCENT: String = "%"

  /** Converts rgb to hsl. */
  @Suppress("FunctionName")
  fun RGBtoHSL(r: Int, g: Int, b: Int, hsl: FloatArray?): FloatArray {
    var res = hsl
    if (res == null) res = FloatArray(3)

    val vals = convertRGBToHSL(r, g, b)
    res[0] = vals[0]
    res[1] = vals[1]
    res[2] = vals[2]
    return res
  }

  /** Parse a color from r,g,b in decimal format. */
  fun getDecimalRGB(r: Int, g: Int, b: Int): Color = getDecimalRGBa(r, g, b, 1.0f)

  /** Parse a color from r,g,b,a in decimal format. */
  fun getDecimalRGBa(r: Int, g: Int, b: Int, a: Float): Color {
    var red = r
    var green = g
    var blue = b
    red = normalizeDecimal(red)
    green = normalizeDecimal(green)
    blue = normalizeDecimal(blue)

    val alpha = toDecimal(normalizeFraction(a))
    return Color(red, green, blue, alpha)
  }

  /** Parse a color from h,s,l in decimal format. */
  fun getHSL(h: Int, s: Int, l: Int): Color = getHSLa(h, s, l, 1.0f)

  /** Get a color from hsl. */
  fun getHSLColor(h: Float, s: Float, l: Float): Color = Color(HSLtoRGB(h, s, l))

  /** Parse a color from h,s,l,a in decimal format. */
  fun getHSLa(h: Int, s: Int, l: Int, a: Float): Color {
    var hue = h
    var saturation = s
    var lightning = l
    var alpha = a

    hue = normalizeDegrees(hue)
    saturation = normalizePercent(saturation)
    lightning = normalizePercent(lightning)
    alpha = normalizeFraction(alpha)

    val rgb = convertHSLToRGB(hue / 359.0f, saturation / 100.0f, lightning / 100.0f)
    return Color(rgb[0], rgb[1], rgb[2], alpha)
  }

  fun getHex(hex: String): Color = when {
    !hex.startsWith(HASH) -> getRGB(hex)
    else -> getRGB(hex.substring(1))
  }

  /** Parse a color from r,g,b in percent format. */
  fun getPercentRGB(r: Int, g: Int, b: Int): Color = getPercentRGBa(r, g, b, 1.0f)

  /** Parse a color from r,g,b,a in percent format. */
  fun getPercentRGBa(r: Int, g: Int, b: Int, a: Float): Color {
    var red = r
    var green = g
    var blue = b
    var alpha = a
    red = normalizePercent(red)
    green = normalizePercent(green)
    blue = normalizePercent(blue)
    alpha = normalizeFraction(alpha)

    return Color(red / 100.0f, green / 100.0f, blue / 100.0f, alpha)
  }

  /** Parse rgb in the hex format #123456. */
  fun getRGB(hex: String): Color {
    val rgb = normalizeRGB(hex, 6)
    val r = rgb.substring(0, 2).toInt(16)
    val g = rgb.substring(2, 4).toInt(16)
    val b = rgb.substring(4, 6).toInt(16)

    return try {
      val a = rgb.substring(6, 8).toInt(16)
      Color(r, g, b, a)
    } catch (e: Exception) {
      Color(r, g, b)
    }
  }

  fun getRGBA(hex: String): Color {
    val rgb = normalizeRGB(hex, 8)
    val rgbaEnabled = ColorHighlighterConfig.instance.isRgbaEnabled

    return when {
      !rgbaEnabled -> getArgb(rgb)
      else -> getRgba(rgb)
    }
  }

  /** Parse rgb in the hex format #123. */
  fun getShortRGB(hex: String): Color {
    val rgb = normalizeRGB(hex, 3)
    val r = rgb.substring(0, 1).repeat(2).toInt(16)
    val g = rgb.substring(1, 2).repeat(2).toInt(16)
    val b = rgb.substring(2, 3).repeat(2).toInt(16)
    return Color(r, g, b)
  }

  fun getSuitableForeground(color: Color?): Gray = if (ColorUtil.isDark(color!!)) Gray._254 else Gray._1

  fun toHSL(color: Color): String {
    val hsl = FloatArray(3)
    RGBtoHSL(color.red, color.green, color.blue, hsl)
    return String.format("hsl(%d, %d%%, %d%%)",
      (hsl[0] * 100).roundToLong(),
      (hsl[1] * 100).roundToLong(),
      (hsl[2] * 100).roundToLong())
  }

  fun toHSLA(color: Color): String {
    val hsl = FloatArray(3)
    RGBtoHSL(color.red, color.green, color.blue, hsl)
    return String.format("hsl(%d, %d%%, %d%%, %d)",
      (hsl[0] * 100).roundToLong(),
      (hsl[1] * 100).roundToLong(),
      (hsl[2] * 100).roundToLong(),
      color.alpha / 255)
  }

  fun toHex(color: Color?): String = ColorUtil.toHex(color!!)

  fun toRGB(color: Color): String = String.format("rgb(%d, %d, %d)", color.red, color.green, color.blue)

  fun toRGBA(color: Color): String =
    String.format("rgb(%s, %s, %s, %d)", color.red, color.green, color.blue, color.alpha / 255)

  /** Converts hsl to rgb. */
  @Suppress("FunctionName")
  private fun HSLtoRGB(h: Float, s: Float, l: Float): Int {
    val vals = convertHSLToRGB(h, s, l)
    val r = (vals[0] * 255.0f + 0.5f).toInt()
    val g = (vals[1] * 255.0f + 0.5f).toInt()
    val b = (vals[2] * 255.0f + 0.5f).toInt()
    return 255 shl 24 or (r shl 16) or (g shl 8) or b
  }

  private fun convertHSLToRGB(h: Float, s: Float, l: Float): FloatArray {
    val res = FloatArray(3)
    val m2 = if (l <= 0.5f) l * (s + 1.0f) else l + s - l * s
    val m1 = l * 2.0f - m2
    res[0] = convertHueToRGB(m1, m2, h + 1.0f / 3.0f)
    res[1] = convertHueToRGB(m1, m2, h)
    res[2] = convertHueToRGB(m1, m2, h - 1.0f / 3.0f)
    return res
  }

  private fun convertHueToRGB(m1: Float, m2: Float, h: Float): Float {
    var hue = h
    if (hue < 0.0f) hue++
    if (hue > 1.0f) hue--

    return when {
      hue * 6.0f < 1.0f -> m1 + (m2 - m1) * hue * 6.0f
      hue * 2.0f < 1.0f -> m2
      hue * 3.0f < 2.0f -> m1 + (m2 - m1) * (2.0f / 3.0f - hue) * 6.0f
      else -> m1
    }
  }

  private fun convertRGBToHSL(r: Int, g: Int, b: Int): FloatArray {
    val res = FloatArray(3)
    val vR = r / 255.0f
    val vG = g / 255.0f
    val vB = b / 255.0f

    val min = min(vR, min(vG, vB))
    val max = max(vR, max(vG, vB))
    val delta = max - min

    val l = (max + min) / 2.0f
    var h: Float
    val s: Float

    if (delta == 0.0f) {
      h = 0.0f
      s = 0.0f
    } else {
      s = if (l < 0.5f) delta / (max + min) else delta / (2.0f - max - min)

      val dR = ((max - vR) / 6.0f + delta / 2.0f) / delta
      val dG = ((max - vG) / 6.0f + delta / 2.0f) / delta
      val dB = ((max - vB) / 6.0f + delta / 2.0f) / delta

      h = when {
        vR == max -> dB - dG
        vG == max -> 1.0f / 3.0f + dR - dB
        else -> 2.0f / 3.0f + dG - dR
      }
      if (h < 0.0f) h++
      if (h > 1.0f) h--
    }
    res[0] = h
    res[1] = s
    res[2] = l
    return res
  }

  private fun getArgb(rgb: String): Color {
    val a = rgb.substring(0, 2).toInt(16)
    val r = rgb.substring(2, 4).toInt(16)
    val g = rgb.substring(4, 6).toInt(16)
    val b = rgb.substring(6, 8).toInt(16)

    return try {
      Color(r, g, b, a)
    } catch (e: Exception) {
      Color(a, r, g)
    }
  }

  private fun getRgba(rgb: String): Color {
    val r = rgb.substring(0, 2).toInt(16)
    val g = rgb.substring(2, 4).toInt(16)
    val b = rgb.substring(4, 6).toInt(16)
    val a = rgb.substring(6, 8).toInt(16)

    return try {
      Color(r, g, b, a)
    } catch (e: Exception) {
      Color(a, r, g)
    }
  }

  /** Ensure a number is between 0 and 255. */
  private fun normalizeDecimal(x: Int): Int = max(0, min(x, 255))

  /** Ensure a number is between 0 and 360. */
  private fun normalizeDegrees(x: Int): Int = max(0, min(x % 360, 359))

  /** Ensure a number is between 0 and 1. */
  private fun normalizeFraction(f: Float): Float = max(0.0f, max(f, 1.0f))

  /** Ensure a number is between 0 and 100. */
  private fun normalizePercent(x: Int): Int = max(0, min(x, 100))

  private fun normalizeRGB(hex: String, desiredLength: Int): String {
    var rgb = hex

    if (rgb.length < desiredLength) {
      rgb = padZeros(desiredLength - rgb.length, rgb)
    } else if (rgb.length > desiredLength) {
      rgb = rgb.substring(0, desiredLength)
    }
    return rgb
  }

  private fun padZeros(num: Int, color: String): String = StringUtil.repeat("0", num) + color

  /** Converts to decimal. */
  private fun toDecimal(f: Float): Int = (f * 255.0f + 0.5f).toInt()

}
