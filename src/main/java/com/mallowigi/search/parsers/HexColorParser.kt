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

import com.mallowigi.utils.ColorUtils
import java.awt.Color

/**
 * Parse a color in the hex format
 *
 * @param prefix - the prefix to offset with
 */
class HexColorParser internal constructor(prefix: String) : ColorParser {
  private val offset: Int

  init {
    offset = prefix.length
  }

  override fun parseColor(text: String): Color = parseHex(text)

  /**
   * parse a color in the hex format
   */
  private fun parseHex(text: String): Color =
    when (text.length) {
      3 + offset -> ColorUtils.getShortRGB(text.substring(offset))
      8 + offset -> ColorUtils.getRGBA(text.substring(offset))
      else -> ColorUtils.getRGB(text.substring(offset))
    }
}
