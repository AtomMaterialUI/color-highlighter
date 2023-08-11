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

package com.mallowigi.visitors

import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.mallowigi.config.home.ColorHighlighterState
import com.mallowigi.search.ColorPrefixes.COLOR_METHOD
import com.mallowigi.search.ColorPrefixes.KT_COLOR
import com.mallowigi.search.ColorSearchEngine
import com.mallowigi.search.parsers.ColorCtorParser
import com.mallowigi.search.parsers.ColorMethodParser
import com.mallowigi.search.parsers.ColorParser
import org.jetbrains.kotlin.psi.KtFile

class KotlinVisitor : ColorVisitor() {

  private val allowedTypes = listOf(
    "INTEGER_CONSTANT",
    "STRING_TEMPLATE",
    "CALL_EXPRESSION",
    "REFERENCE_EXPRESSION"
  )

  private val config = ColorHighlighterState.instance

  override fun clone(): HighlightVisitor = KotlinVisitor()

  override fun getParser(text: String): ColorParser = when {
    text.startsWith(KT_COLOR.text) -> ColorCtorParser()
    text.startsWith(COLOR_METHOD.text) -> ColorMethodParser(COLOR_METHOD.text)
    else -> throw IllegalArgumentException("Cannot find a parser for the text: $text")
  }

  override fun shouldParseText(text: String): Boolean {
    // todo add settings for those
//    val prefixes = setOf(
//      JBCOLOR.text,
//      COLOR_ARGB.text,
//      COLOR_RGB.text
//    )

    return when {
      config.isKotlinColorCtorEnabled -> text.startsWith(KT_COLOR.text)
      config.isKotlinColorMethodEnabled -> text.startsWith(COLOR_METHOD.text)
      else -> false
    }

//    return prefixes.any { text.startsWith(it) }
  }

  override fun suitableForFile(file: PsiFile): Boolean = file is KtFile

  override fun visit(element: PsiElement) {
    val type = PsiUtilCore.getElementType(element).toString()
    if (type !in allowedTypes) return

    val value = element.text
    val color = ColorSearchEngine.getColor(value, this)
    color?.let { highlight(element, it) }
  }

}
