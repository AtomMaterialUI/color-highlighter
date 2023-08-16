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
 * @property startParen
 * @property endParen
 */
@Suppress("DuplicatedCode")
class ColorData(
  var startParen: Int = -1,
  var endParen: Int = -1
) {

  /** initialization */
  fun init(text: String) {
    startParen = text.indexOf('(')
    endParen = text.indexOf(')')
  }

  /** Parse one component part of a `Color(r,g,b)` form. */
  fun parseComponent(part: String): Any = when {
    "true" == part -> true
    "false" == part -> false
    part.endsWith("f") -> part.substring(0, part.length - 1).toFloat()
    part.contains(".") -> part.toFloat()
    part.endsWith("%") -> part.substring(0, part.length - 1).toInt() / 100.0f
    else -> parseInt(part)
  }

  /** Parse single component of a `Color(h,s,l)` form. */
  fun parseHSLComponent(part: String): Float = when {
    part.endsWith("%") -> part.substring(0, part.length - 1).toFloat()
    else -> parseFloat(part)
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

  private fun parseInt(part: @NonNls String?): Int = when {
    part!!.lowercase(Locale.getDefault()).startsWith("0x") -> part.substring(2).toInt(16)
    part.startsWith("0") && part.length > 1 -> part.substring(1).toInt(8)
    else -> part.toInt()
  }
}
