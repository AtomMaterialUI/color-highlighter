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

package com.mallowigi.colors

import org.jetbrains.annotations.NonNls
import java.util.*


class JavaColorCtor(
  var isFloat: Boolean = false,
  var floatRed: Float = 0.0f,
  var floatGreen: Float = 0.0f,
  var floatBlue: Float = 0.0f,
  var floatAlpha: Float = 1.0f,
  var intRed: Int = 0,
  var intGreen: Int = 0,
  var intBlue: Int = 0,
  var intAlpha: Int = 255,
  var alpha: Boolean = false,
  var startParen: Int = -1,
  var endParen: Int = -1
) {

  fun parseRed(part: String): Unit = when {
    part.endsWith("f") -> {
      isFloat = true
      floatRed = part.substring(0, part.length - 1).toFloat()
    }
    else -> intRed = parseInt(part)
  }

  fun parseGreen(part: String): Unit = when {
    "true" == part -> alpha = true
    "false" == part -> alpha = false
    part.endsWith("f") -> {
      isFloat = true
      floatGreen = part.substring(0, part.length - 1).toFloat()
    }
    else -> intGreen = parseInt(part)
  }

  fun parseBlue(part: String): Unit = when {
    part.endsWith("f") -> {
      isFloat = true
      floatBlue = part.substring(0, part.length - 1).toFloat()
    }
    else -> intBlue = parseInt(part)
  }

  fun parseAlpha(part: String): Unit = when {
    part.endsWith("f") -> {
      isFloat = true
      floatAlpha = part.substring(0, part.length - 1).toFloat()
    }
    else -> intAlpha = parseInt(part)
  }

  fun getNextNumber(tokenizer: StringTokenizer): String = tokenizer.nextToken().trim { it <= ' ' }

  private fun parseInt(part: @NonNls String?): Int {
    return when {
      part!!.lowercase(Locale.getDefault()).startsWith("0x") -> part.substring(2).toInt(16)
      part.startsWith("0") && part.length > 1 -> part.substring(1).toInt(8)
      else -> part.toInt()
    }
  }
}
