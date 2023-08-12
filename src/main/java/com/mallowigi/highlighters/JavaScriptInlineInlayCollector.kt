/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2023 Elior "Mallowigi" Boukhobza
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

package com.mallowigi.highlighters

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.mallowigi.ColorHighlighterIcons
import com.mallowigi.utils.themedIcon
import com.mallowigi.visitors.JSVisitor
import java.awt.Color

@Suppress("UnstableApiUsage")
class JavaScriptInlineInlayCollector(editor: Editor) : FactoryInlayHintsCollector(editor) {

  override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
    val visitor = JSVisitor()
    val acceptable: Boolean = visitor.accept(element)

    if (acceptable) {
      var startOffset = element.textRange.startOffset
      val lineNumber = editor.document.getLineNumber(startOffset)
      val lineStartOffset = editor.document.getLineStartOffset(lineNumber)

      startOffset -= lineStartOffset

      val icon = factory.icon(ColorHighlighterIcons.Other.INLINE.themedIcon(Color.RED))

      sink.addInlineElement(
        lineStartOffset + startOffset,
        false,
        factory.inset(
          icon,
          right = 1,
          top = 4
        ),
        false
      )

      return true
    }

    return true
  }


}