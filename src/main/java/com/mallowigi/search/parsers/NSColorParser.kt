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

import com.mallowigi.colors.ColorData
import java.awt.Color
import java.util.*

/**
 * Parse a color in the objectiveC format
 *
 * TODO
 *
 */
class NSColorParser : ColorParser {
  override fun parseColor(text: String?): Color? = parseMethodCall(text!!)

  private fun parseMethodCall(text: String): Color? {
    val colorData = ColorData()
    colorData.run {
      startParen = 9 // "[NScolor "
      endParen = text.indexOf(']')

      if (startParen == -1 || endParen == -1) return null

      // tokenize the string
      val tokenizer = StringTokenizer(text.substring(startParen + 1, endParen), " ")
      val params = tokenizer.countTokens()
      var next = getNextNumber(tokenizer)
      // extract the part after the :
      var param = getNextParam(next)

      // float support: sets floatRed/intRed
      parseHue(param)

      if (params >= 2) {
        next = getNextNumber(tokenizer)
        param = getNextParam(next)
        parseSaturation(param)
      }

      if (params >= 3) {
        next = getNextNumber(tokenizer)
        param = getNextParam(next)
        parseBrightness(param)
      }

//      if (params == 4) {
//        next = getNextNumber(tokenizer)
//        param = getNextParam(next)
//        parseAlpha(param)
//      }

      return Color.getHSBColor(floatHue, floatSaturation, floatBrightness)
    }
  }
}


