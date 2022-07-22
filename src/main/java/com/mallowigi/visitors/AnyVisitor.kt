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
import com.intellij.psi.PsiLiteralValue
import com.mallowigi.search.ColorSearchEngine

class AnyVisitor : ColorVisitor() {

  override fun suitableForFile(file: PsiFile): Boolean =
    !extensions.contains(file.virtualFile.extension)

  override fun visit(element: PsiElement) {
    if (element !is PsiLiteralValue) return

    val value = element.value
    if (value is String) {
      val color = ColorSearchEngine.getColor((value as String?)!!, this)
      color?.let { highlight(element, it) }
    }
  }

  override fun clone(): HighlightVisitor = AnyVisitor()

  companion object {
    val extensions: Set<String> = setOf(
      "js",
      "cjs",
      "mjs",
      "jsx",
      "dart",
      "tsx",
      "ts",
      "java",
      "kt",
      "php",
      "phpt",
      "py",
      "rb",
      "rbs",
      "go",
      "swift",
      "objc",
      "scala",
      "json",
      "md",
      "mdx",
      "properties",
      "yml",
      "yaml",
      "asp",
      "cs")
  }
}
