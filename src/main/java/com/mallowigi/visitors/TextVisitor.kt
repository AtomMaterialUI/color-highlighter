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
import com.mallowigi.config.home.ColorHighlighterConfig
import com.mallowigi.search.ColorSearchEngine

class TextVisitor : ColorVisitor() {

  override fun clone(): HighlightVisitor = TextVisitor()

  override fun suitableForFile(file: PsiFile): Boolean =
    file.name.matches(".*\\.(txt|log|rst)$".toRegex())

  override fun visit(element: PsiElement) {
    if (!ColorHighlighterConfig.instance.isTextEnabled) return
    val value = element.text
    if (value is String) splitText(value)
  }

  private fun splitText(text: String) {
    val blocks = text.split(Regex("\\b"))
    var cursor = 0
    blocks.forEach { block ->
      visitElement(Pair(IntRange(cursor, cursor + block.length), block))
      cursor += block.length
    }
  }

  private fun visitElement(element: Pair<IntRange, String>) {
    val (range, text) = element
    val color = ColorSearchEngine.getColor(text, this)
    color?.let { highlight(it, range) }
  }

}

