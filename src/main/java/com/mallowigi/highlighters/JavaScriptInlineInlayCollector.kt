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
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.mallowigi.ColorHighlighterIcons.Settings.MAIN_ICON
import com.mallowigi.visitors.JSVisitor

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

      var factory: PresentationFactory
      val presentation = PresentationFactory(editor)
        .also { factory = it }
        .textSpacePlaceholder(startOffset, true)

      val icon = factory.smallScaledIcon(MAIN_ICON)
      val space = factory.textSpacePlaceholder(1, true)
      val text = factory.smallText("Blabla")

      sink.addInlineElement(startOffset,
        true,
        factory.inset(presentation, left = 1, right = 1),
        false
      )
    }

    return true
  }
}