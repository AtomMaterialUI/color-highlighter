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
package com.mallowigi.search.parsers

import com.mallowigi.colors.ColorData
import com.mallowigi.utils.ColorUtils
import java.awt.Color
import java.util.*

/** Parse text in the form `rgb(r,g,b[,a])` */
class RGBColorParser : ColorParser {

  override fun parseColor(text: String): Color? = parseRGB(text)

  /** Parse a color in the rgb[a](r, g, b[, a]) format. */
  fun parseRGB(text: String): Color? {
    val colorData = ColorData()
    colorData.run {
      init(text)
      if (startParen == -1 || endParen == -1) return null

      // tokenize the string into "red,green,blue"
      val tokenizer = StringTokenizer(text.substring(startParen + 1, endParen), ",")
      val params = tokenizer.countTokens()
      if (params < 3 || params > 4) return null

      val components = (1..params).map { parseComponent(getNextNumber(tokenizer)) }

      fun asFloat(x: Any) = when {
        x is Int -> x.toFloat()
        x is Float -> x
        else -> 0f
      }
      fun alphaAsFloat(x: Any) = when {
        x is Int -> x.toFloat() / 255.0f
        x is Float -> x
        else -> 0f
      }

      if (components.take(3).all { it is Int }) {
        val intComponents = components.map { it as Int }
        return when {
          params == 3 -> ColorUtils.getDecimalRGB(intComponents[0], intComponents[1], intComponents[2])
          else -> ColorUtils.getDecimalRGBa(intComponents[0], intComponents[1], intComponents[2], alphaAsFloat(components[3]))
        }
      } else {
        val floatComponents = components.map { asFloat(it) }
        return when {
          params == 3 -> ColorUtils.getFloatRGBa(floatComponents[0], floatComponents[1], floatComponents[2], 1f)
          else -> ColorUtils.getFloatRGBa(floatComponents[0], floatComponents[1], floatComponents[2], asFloat(components[3]))        }
      }

    }

  }

}

