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
import java.awt.Color
import java.util.*

/** Parses colors in the form `Color(a,b,c)` */
class ColorCtorParser : ColorParser {

  override fun parseColor(text: String): Color? = parseConstructor(text)

  private fun parseConstructor(text: String): Color? {
    val colorData = ColorData()
    colorData.run {
      init(text)

      if (startParen == -1 || endParen == -1) return null

      // tokenize the string into "red,green,blue"
      val tokenizer = StringTokenizer(text.substring(startParen + 1, endParen), ",")
      val params = tokenizer.countTokens()
      if (params < 1 || params > 4) return null

      return when (params) {
        1    -> {// single hex int
          val hex = parseComponent(getNextNumber(tokenizer)) as Int
          Color(hex)
        }

        2    -> {// hex int followed with hasAlpha
          val hex = parseComponent(getNextNumber(tokenizer)) as Int
          val hasAlpha = parseComponent(getNextNumber(tokenizer)) as Boolean
          Color(hex, hasAlpha)
        }

        else -> RGBColorParser().parseRGB(text)
      }
    }
  }

}


