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
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.childLeafs
import com.intellij.psi.util.firstLeaf
import com.intellij.psi.util.parentOfType
import com.jetbrains.lang.dart.DartTokenTypes
import com.jetbrains.lang.dart.psi.DartRecursiveVisitor
import com.jetbrains.lang.dart.psi.DartReference
import com.jetbrains.lang.dart.psi.DartVarDeclarationList
import com.jetbrains.lang.dart.psi.DartVarInit
import com.mallowigi.search.ColorSearchEngine
import org.rust.lang.core.psi.ext.descendantOfType
import org.toml.lang.psi.ext.elementType
import java.awt.Color

class DartVisitor : ColorVisitor() {

  private val allowedTypes = setOf("REGULAR_STRING_PART", "NUMBER", "REFERENCE_EXPRESSION")

  override fun clone(): HighlightVisitor = DartVisitor()

  override fun suitableForFile(file: PsiFile): Boolean =
    file.name.matches(".*\\.dart$".toRegex())

  override fun accept(element: PsiElement): Color? {
    val type = PsiUtilCore.getElementType(element).toString()
    if (type !in allowedTypes) return null

    if (element is DartReference) {
      if (element.parentOfType<DartReference>()?.resolve() == element.resolve()) return null
      return element.readReferencedValue()
    } else {
      return ColorSearchEngine.getColor(element.text, this)
    }
  }

  private fun createVisitor(): ColorInitializer = ColorInitializer(this)

  private fun DartReference.readReferencedValue(): Color? {
    if (this.firstLeaf().let { it is LeafPsiElement && it.elementType == DartTokenTypes.IDENTIFIER }) {
      val visitor = createVisitor()
      this.resolve()?.let { visitor.visitElement(it) }
      return visitor.result
    }
    return null
  }
}


class ColorInitializer(private val visitor: DartVisitor) : DartRecursiveVisitor() {
  var result: Color? = null

  override fun visitElement(element: PsiElement) {
    result =
      element.parentOfType<DartVarDeclarationList>()?.descendantOfType<DartVarInit> { true }?.childLeafs()
        ?.filter { it !is PsiWhiteSpace && it.elementType != DartTokenTypes.EQ }?.withIndex()
        ?.filter { (index, value) ->
          val elementType = PsiUtilCore.getElementType(value)
          index == 0 && elementType == DartTokenTypes.IDENTIFIER ||
            index == 2 && elementType == DartTokenTypes.NUMBER
        }?.last()?.let {
          ColorSearchEngine.getColor(
            it.value.descendantOfType<LeafPsiElement> { true }!!.text,
            visitor
          )
        }
    if (result == null) element.acceptChildren(this) else return
  }
}
