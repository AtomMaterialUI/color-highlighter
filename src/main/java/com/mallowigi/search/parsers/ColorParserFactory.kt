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

import com.mallowigi.config.custom.CustomColorsConfig
import com.mallowigi.config.home.ColorHighlighterState
import com.mallowigi.search.ColorPrefixes.*
import com.mallowigi.visitors.LangVisitor

/**
 * Color parser factory: get the color parser according to the text pattern
 *
 * @constructor Create empty Color parser factory
 */
object ColorParserFactory {

  private const val HASH: String = "#"
  private val NO_HEX_PATTERN = """(\b[a-fA-F0-9]{6,8}\b)""".toRegex()
  private val TUPLE_PATTERN = """\((\d{1,3}(,\s*)?){3,4}\)""".toRegex()
  private val config = ColorHighlighterState.instance

  fun getParser(text: String, langVisitor: LangVisitor): ColorParser {
    val customColors = CustomColorsConfig.instance.customColors

    return when {
      text.startsWith(HASH) && text.length > 1 -> HexColorParser(HASH)
      text.startsWith(RGB.text) -> RGBColorParser()
      text.startsWith(HSL.text) -> HSLColorParser()
      text.startsWith(OX.text) -> HexColorParser(OX.text)

      // Tuple detection
      config.isTupleDetectEnabled && TUPLE_PATTERN.matches(text) -> RGBColorParser()

      // Hex with no hash detection
      config.isHexDetectEnabled && NO_HEX_PATTERN.matches(text) -> HexColorParser("")

      // If the lang visitor should parse the text, retrieve the parser
      langVisitor.shouldParseText(text) -> langVisitor.getParser(text) ?: PredefinedColorParser()

      // custom colors
      config.isColorNamesDetectEnabled && text in customColors -> CustomColorParser()

      // Parse from PredefinedColors
      config.isColorNamesDetectEnabled -> PredefinedColorParser()

      else -> NoParser()
    }
  }

}
