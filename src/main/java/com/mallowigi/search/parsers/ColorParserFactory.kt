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

import com.mallowigi.search.ColorPrefixes.*
import com.mallowigi.visitors.LangVisitor

object ColorParserFactory {

  private val NO_HEX_PATTERN = """(\\b[a-fA-F0-9]{6,8}\\b)""".toRegex()
  private const val HASH: String = "#"

  fun getParser(text: String, langVisitor: LangVisitor): ColorParser {
    return when {
      text.startsWith(HASH) && text.length > 1 -> HexColorParser(HASH)
      text.startsWith(RGB.text) -> RGBColorParser()
      text.startsWith(HSL.text) -> HSLColorParser()
      text.startsWith(OX.text) -> HexColorParser(OX.text)
      NO_HEX_PATTERN.matches(text) -> HexColorParser("")

      // If the lang visitor should parse the text, retrieve the parser
      langVisitor.shouldParseText(text) -> langVisitor.getParser(text) ?: SVGColorParser()


      else -> SVGColorParser()
    }
  }
}
