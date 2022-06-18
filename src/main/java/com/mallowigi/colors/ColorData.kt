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

package com.mallowigi.colors

import org.jetbrains.annotations.NonNls
import java.lang.Float.parseFloat
import java.util.*


/**
 * This data class serves as a container for color parsing. Used in the
 * different parsers.
 *
 * @property isPercent
 * @property isFloat
 * @property floatRed
 * @property floatGreen
 * @property floatBlue
 * @property floatHue
 * @property floatSaturation
 * @property floatBrightness
 * @property floatAlpha
 * @property intRed
 * @property intGreen
 * @property intBlue
 * @property intAlpha
 * @property alpha
 * @property startParen
 * @property endParen
 */
@Suppress("DuplicatedCode")
data class ColorData(
  var isPercent: Boolean = false,
  var isFloat: Boolean = false,
  var floatRed: Float = 0.0f,
  var floatGreen: Float = 0.0f,
  var floatBlue: Float = 0.0f,
  var floatHue: Float = 0.0f,
  var floatSaturation: Float = 0.0f,
  var floatBrightness: Float = 0.0f,
  var floatAlpha: Float = 1.0f,
  var intRed: Int = 0,
  var intGreen: Int = 0,
  var intBlue: Int = 0,
  var intAlpha: Int = 255,
  var alpha: Boolean = false,
  var startParen: Int = -1,
  var endParen: Int = -1
) {

  /** initialization */
  fun init(text: String) {
    startParen = text.indexOf('(')
    endParen = text.indexOf(')')
  }

  /** Parse red part of a `Color(r,g,b)` form. */
  fun parseRed(part: String): Unit = when {
    part.endsWith("f") -> {
      isFloat = true
      floatRed = part.substring(0, part.length - 1).toFloat()
    }

    part.endsWith("%") -> {
      isPercent = true
      intRed = part.substring(0, part.length - 1).toInt()
    }

    else -> intRed = parseInt(part)
  }

  /** Parse green part of a `Color(r,g,b)` form. */
  fun parseGreen(part: String): Unit = when {
    "true" == part -> alpha = true
    "false" == part -> alpha = false
    part.endsWith("f") -> {
      isFloat = true
      floatGreen = part.substring(0, part.length - 1).toFloat()
    }

    part.endsWith("%") -> {
      isPercent = true
      intGreen = part.substring(0, part.length - 1).toInt()
    }

    else -> intGreen = parseInt(part)
  }

  /** Parse blue part of a `Color(r,g,b)` form. */
  fun parseBlue(part: String): Unit = when {
    part.endsWith("f") -> {
      isFloat = true
      floatBlue = part.substring(0, part.length - 1).toFloat()
    }

    part.endsWith("%") -> {
      isPercent = true
      intBlue = part.substring(0, part.length - 1).toInt()
    }

    else -> intBlue = parseInt(part)
  }

  /** Parse alpha part of a `Color(r,g,b,a)` form. */
  fun parseAlpha(part: String): Unit = when {
    part.endsWith("f") || part.contains('.') -> {
      isFloat = true
      floatAlpha = part.substring(0, part.length - 1).toFloat()
    }

    part.endsWith("%") -> {
      isPercent = true
      floatAlpha = part.substring(0, part.length - 1).toFloat()
    }

    else -> intAlpha = parseInt(part)
  }

  /** Parse hue part of a `Color(h,s,l)` form. */
  fun parseHue(part: String): Unit = when {
    part.endsWith("%") -> {
      isPercent = true
      floatHue = part.substring(0, part.length - 1).toFloat()
    }

    else -> floatHue = parseFloat(part)
  }


  /** Parse saturation part of a `Color(h,s,l)` form. */
  fun parseSaturation(part: String): Unit = when {
    part.endsWith("%") -> {
      isPercent = true
      floatSaturation = part.substring(0, part.length - 1).toFloat()
    }

    else -> floatSaturation = parseFloat(part)
  }


  /** Parse brightness part of a `Color(h,s,l)` form. */
  fun parseBrightness(part: String): Unit = when {
    part.endsWith("%") -> {
      isPercent = true
      floatBrightness = part.substring(0, part.length - 1).toFloat()
    }

    else -> floatBrightness = parseFloat(part)
  }


  /**
   * Get next number of a `Color(x,y,z)` form
   *
   * @param tokenizer the string tokenizer
   * @return the next number token
   */
  fun getNextNumber(tokenizer: StringTokenizer): String = tokenizer.nextToken().trim { it <= ' ' }

  /**
   * Get param of a [NSColor key:param] form
   *
   * @param next the key:value part
   * @return the part after the :
   */
  fun getNextParam(next: String): String = next.split(":")[1]

  private fun parseInt(part: @NonNls String?): Int {
    return when {
      part!!.lowercase(Locale.getDefault()).startsWith("0x") -> part.substring(2).toInt(16)
      part.startsWith("0") && part.length > 1 -> part.substring(1).toInt(8)
      else -> part.toInt()
    }
  }
}
