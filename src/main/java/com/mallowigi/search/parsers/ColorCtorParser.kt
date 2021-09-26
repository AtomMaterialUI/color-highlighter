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

import org.jetbrains.annotations.NonNls
import java.awt.Color
import java.util.*

class ColorCtorParser : ColorParser {
  override fun parseColor(text: String?): Color? = parseConstructor(text!!)

  private fun parseConstructor(text: String): Color? {
    var isFloat = false
    var fr = 0.0f
    var fg = 0.0f
    var fb = 0.0f
    var fa = 1.0f
    var ir = 0
    var ig = 0
    var ib = 0
    var ia = 255
    var alpha = false
    val ps = text.indexOf('(')
    val pe = text.indexOf(')')
    if (ps == -1 || pe == -1) {
      return null
    }
    val tokenizer = StringTokenizer(text.substring(ps + 1, pe), ",")
    val params = tokenizer.countTokens()
    var part = tokenizer.nextToken().trim { it <= ' ' }
    if (part.endsWith("f")) {
      isFloat = true
      fr = part.substring(0, part.length - 1).toFloat()
    } else {
      ir = parseInt(part)
    }
    if (params >= 2) {
      part = tokenizer.nextToken().trim { it <= ' ' }
      if ("true" == part) {
        alpha = true
      } else if ("false" == part) {
        alpha = false
      } else if (part.endsWith("f")) {
        isFloat = true
        fg = part.substring(0, part.length - 1).toFloat()
      } else {
        ig = parseInt(part)
      }
      if (params >= 3) {
        part = tokenizer.nextToken().trim { it <= ' ' }
        if (part.endsWith("f")) {
          isFloat = true
          fb = part.substring(0, part.length - 1).toFloat()
        } else {
          ib = parseInt(part)
        }
        if (params == 4) {
          part = tokenizer.nextToken().trim { it <= ' ' }
          if (part.endsWith("f")) {
            isFloat = true
            fa = part.substring(0, part.length - 1).toFloat()
          } else {
            ia = parseInt(part)
          }
        }
      }
    }
    return when {
      isFloat -> Color(fr, fg, fb, fa)
      else -> when (params) {
        1 -> Color(ir)
        2 -> Color(ir, alpha)
        else -> Color(ir, ig, ib, ia)
      }
    }
  }

  private fun parseInt(part: @NonNls String?): Int {
    val res: Int = when {
      part!!.lowercase(Locale.getDefault()).startsWith("0x") -> part.substring(2).toInt(16)
      part.startsWith("0") && part.length > 1 -> part.substring(1).toInt(8)
      else -> part.toInt()
    }
    return res
  }
}
