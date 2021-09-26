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
package com.mallowigi.search.parsers

import com.mallowigi.utils.ColorUtils
import java.awt.Color
import java.util.*

class RGBColorParser : ColorParser {
  override fun parseColor(text: String?): Color? = parseRGB(text!!)

  /**
   * Parse a color in the rgb[a](r, g, b[, a]) format
   */
  private fun parseRGB(text: String): Color? {
    var isPercent = false
    var a = 1.0f
    val red: Int
    val green: Int
    val blue: Int
    val parenStart = text.indexOf(ColorUtils.OPEN_PAREN)
    val parenEnd = text.indexOf(ColorUtils.CLOSE_PAREN)
    if (parenStart == -1 || parenEnd == -1) {
      return null
    }
    val tokenizer = StringTokenizer(text.substring(parenStart + 1, parenEnd), ColorUtils.COMMA) // split by ,
    if (tokenizer.countTokens() < 3) {
      return null
    }

    // Parse r, g, b and a
    var part = tokenizer.nextToken().trim { it <= ' ' }
    if (part.endsWith(ColorUtils.PERCENT)) {
      isPercent = true
      red = part.substring(0, part.length - 1).toInt()
    } else {
      red = part.toInt()
    }
    part = tokenizer.nextToken().trim { it <= ' ' }
    if (part.endsWith(ColorUtils.PERCENT)) {
      isPercent = true
      green = part.substring(0, part.length - 1).toInt()
    } else {
      green = part.toInt()
    }
    part = tokenizer.nextToken().trim { it <= ' ' }
    if (part.endsWith(ColorUtils.PERCENT)) {
      isPercent = true
      blue = part.substring(0, part.length - 1).toInt()
    } else {
      blue = part.toInt()
    }
    if (tokenizer.hasMoreTokens()) {
      part = tokenizer.nextToken().trim { it <= ' ' }
      a = part.toFloat()
    }
    return if (isPercent) ColorUtils.getPercentRGBa(red, green, blue, a) else ColorUtils.getDecimalRGBa(red, green, blue, a)
  }
}

