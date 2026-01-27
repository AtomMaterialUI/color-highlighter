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

package com.mallowigi.search

import com.mallowigi.search.parsers.ColorParserFactory
import com.mallowigi.visitors.ColorVisitor
import java.awt.Color
import java.util.regex.Pattern

object ColorSearchEngine {
  // region COLOR_PATTERNS
  private val COLOR_PATTERNS = listOf(
    Pattern.compile(
      "\\b((#?\\p{XDigit}{8}\\b)|(#?\\p{XDigit}{6}\\b)|(#?\\p{XDigit}{3}\\b))"
    ),  // #12345678, #123456 or #333
    Pattern.compile(
      "\\b((0[xX])\\p{XDigit}{8}\\b|(0[xX])\\p{XDigit}{6}\\b)"
    ),  // 0x12345678 or 0x123456
    Pattern.compile(
      "\\b((rgb\\s*\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\))|(rgb\\s*\\(\\s*\\d{1," +
        "3}%\\s*,\\s*\\d{1,3}%\\s*,\\s*\\d{1,3}%\\s*\\)))"
    ),  // rgb(128, 128, 128)
    Pattern.compile(
      "\\b((rgba\\s*\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*[0-9.]{1,3}\\s*\\))|" +
        "(rgba\\s*\\" +
        "(\\s*\\d{1,3}%\\s*,\\s*\\d{1,3}%\\s*,\\s*\\d{1,3}%\\s*,\\s*[0-9.]{1,3}\\s*\\)))"
    ),  // rgba(128, 128, 128, 0)
    Pattern.compile(
      "\\b(hsl\\s*\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}%\\s*,\\s*\\d{1,3}%\\s*\\))"
    ),  // hsl(0, 12, 120)
    Pattern.compile(
      "\\b(hsla\\s*\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}%\\s*,\\s*\\d{1,3}%\\s*,\\s*[0-9.]{1,3}\\s*\\))"
    ),  // hsla(0, 12, 120, 1)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
    ),  // Color(0x123456)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(true|false)\\s*\\))"
    ),  // Color(0x123456, true)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
    ),  // Color(128, 0, 12)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])" +
        "?[0-9a-fA-F]+\\s*\\))"
    ),  // Color(123, 0, 12, 12)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
    ),  // Color(12f, 32f, 9f)
    Pattern.compile(
      "\\b(Color\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
    ),  // Color(12f, 32f, 9f, 0f)
    Pattern.compile(
      "\\b(Color\\.[a-zA-Z_]+)\\b"
    ),  // Color.yellow
    Pattern.compile(
      "\\b(ColorUIResource\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
    ),  // ColorUIResource(12f, 12f, 12f)
    Pattern.compile(
      "\\b(ColorUIResource\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
    ),  // ColorUIResource(0x123456)
    Pattern.compile(
      "\\b(ColorUIResource\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
    ),  // ColorUIResource(12f, 12f, 12f)
    Pattern.compile(
      "\\b(ColorUIResource\\.[a-zA-Z_]+)\\b"
    ),  // ColorUIResource.black
    Pattern.compile(
      "\\b([a-zA-Z]+)\\b"
    ) // aliceblue
  )
  // endregion

  /**
   * Try to parse a color using the provided formats.
   *
   * @param text text to parse
   * @param visitor a Language Visitor to provide additional formats (ex: Color() for Java/Kotlin)
   */
  fun getColor(text: String, visitor: ColorVisitor): Color? = try {
    val normalizedText = text.replace("\"".toRegex(), "").replace("'".toRegex(), "")
    ColorParserFactory.getParser(normalizedText, visitor).parseColor(normalizedText)
  } catch (e: RuntimeException) {
    null
  }

  /**
   * Try to find colors in the given text.
   *
   * @param text text to parse
   * @param visitor a Language Visitor to provide additional formats (ex: Color() for Java/Kotlin)
   */
  fun getAllColors(text: String, visitor: ColorVisitor): List<ColorMatch> {
    val matches = mutableListOf<ColorMatch>()
    val normalizedText = text.replace("\"".toRegex(), " ").replace("'".toRegex(), " ")

    for (pattern in COLOR_PATTERNS) {
      val matcher = pattern.matcher(normalizedText)
      while (matcher.find()) {
        val group = matcher.group()
        val color = try {
          ColorParserFactory.getParser(group, visitor).parseColor(group)
        } catch (e: Exception) {
          null
        }

        if (color != null) {
          matches.add(ColorMatch(matcher.start()..matcher.end(), color))
        }
      }
    }
    return matches
  }

}