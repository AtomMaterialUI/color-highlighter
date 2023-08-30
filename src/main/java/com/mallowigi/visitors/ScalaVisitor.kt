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
import com.mallowigi.search.ColorPrefixes
import com.mallowigi.search.ColorSearchEngine
import com.mallowigi.search.parsers.ColorCtorParser
import com.mallowigi.search.parsers.ColorMethodParser
import com.mallowigi.search.parsers.ColorParser
import org.jetbrains.plugins.scala.lang.psi.api.ScalaFile
import org.jetbrains.plugins.scala.lang.psi.api.base.ScConstructorInvocation
import org.jetbrains.plugins.scala.lang.psi.api.base.literals.ScIntegerLiteral
import org.jetbrains.plugins.scala.lang.psi.api.base.literals.ScStringLiteral
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScReferenceExpression
import java.awt.Color
import kotlin.reflect.KClass

class ScalaVisitor : ColorVisitor() {

  private val parsedTypes = mapOf<KClass<*>, ((PsiElement) -> Color?)?>(
    ScIntegerLiteral::class to ::parseColor,
    ScStringLiteral::class to ::parseColor,
    ScConstructorInvocation::class to ::parseCtor,
    ScReferenceExpression::class to ::parseMethod,
  )

  private fun parseCtor(element: PsiElement): Color? {
    val text = element.text
    return when {
      config.isScalaColorCtorEnabled && text.startsWith(ColorPrefixes.KT_COLOR.text) -> ColorCtorParser().parseColor(text)
      else -> null
    }
  }

  private fun parseMethod(element: PsiElement): Color? {
    val text = element.text
    return when {
      config.isScalaColorMethodEnabled && text.startsWith(ColorPrefixes.COLOR_METHOD.text) -> ColorMethodParser(ColorPrefixes.COLOR_METHOD.text).parseColor(text)
      else -> null
    }
  }

  private fun parseColor(element: PsiElement): Color? {
    val value = element.text
    return ColorSearchEngine.getColor(value, this)
  }

  override fun clone(): HighlightVisitor = ScalaVisitor()

  override fun suitableForFile(file: PsiFile): Boolean = file is ScalaFile

  override fun accept(element: PsiElement): Color? {
    for (keyClass in parsedTypes.keys) {
      if (keyClass.isInstance(element)) {
        return try {
          parsedTypes[keyClass]?.invoke(element)
        } catch (e: Exception) {
          return null;
        }
      }
    }
    return null
  }

}
