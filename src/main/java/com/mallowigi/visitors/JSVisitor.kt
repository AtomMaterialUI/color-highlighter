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
import com.mallowigi.search.ColorSearchEngine

class JSVisitor : ColorVisitor() {

  private val allowedTypes = listOf(
    "JS:NUMERIC_LITERAL",
    "JS:STRING_LITERAL"
  )

  override fun accept(element: PsiElement): Boolean {
    val type = PsiUtilCore.getElementType(element).toString()
    if (type !in allowedTypes) return false

    val value = element.text
    val color = ColorSearchEngine.getColor(value!!, this)
    return color != null
  }

  override fun clone(): HighlightVisitor = JSVisitor()

  override fun suitableForFile(file: PsiFile): Boolean = true

  override fun visit(element: PsiElement) {
    val type = PsiUtilCore.getElementType(element).toString()
    if (type !in allowedTypes) return

    val value = element.text
    val color = ColorSearchEngine.getColor(value!!, this)
    color?.let { highlight(element, it) }
  }
}