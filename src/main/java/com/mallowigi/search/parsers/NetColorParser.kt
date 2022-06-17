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

/**
 * Parses colors in the form `Color.FromArgb(a,b,c)`
 *
 */
class NetColorParser(val prefix: String) : ColorParser {
  override fun parseColor(text: String?): Color? = parseConstructor(text!!)

  private fun parseConstructor(text: String): Color? {
    val colorData = ColorData()
    colorData.run {
      init(text)

      if (startParen == -1 || endParen == -1) return null

      // tokenize the string into "alpha,red,green,blue"
      val tokenizer = StringTokenizer(text.substring(startParen + 1, endParen), ",")
      val params = tokenizer.countTokens()

      getNextNumber(tokenizer).also { parseAlpha(it) }
      if (params >= 2) getNextNumber(tokenizer).also { parseRed(it) }
      if (params >= 3) getNextNumber(tokenizer).also { parseGreen(it) }
      if (params == 4) getNextNumber(tokenizer).also { parseBlue(it) }

      return when {
        isFloat -> Color(floatRed, floatGreen, floatBlue, floatAlpha)
        else -> when (params) {
          1 -> Color(intRed)
          2 -> Color(intRed, alpha)
          else -> Color(intRed, intGreen, intBlue, intAlpha)
        }
      }
    }
  }
}


