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

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightInfoType.HighlightInfoTypeImpl
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorUtil
import com.intellij.ui.Gray
import com.mallowigi.gutter.GutterColorLineMarkerProvider
import com.mallowigi.gutter.GutterColorRenderer
import java.awt.Color

internal object ColorHighlighter {

  private val COLOR_ELEMENT: HighlightInfoType = HighlightInfoTypeImpl(
    HighlightSeverity.INFORMATION,
    DefaultLanguageHighlighterColors.CONSTANT
  )

  fun highlightColor(element: PsiElement?, color: Color): HighlightInfo? =
    getHighlightInfoBuilder(color).range(element!!).create()

  fun highlightColor(range: IntRange, color: Color): HighlightInfo? = getHighlightInfoBuilder(color)
    .range(range.first, range.last)
    .create()

  @Suppress("Detekt:MagicNumber")
  private fun getAttributesFlyweight(color: Color): TextAttributes {
    val attributes = TextAttributes()
    val background = EditorColorsManager.getInstance().globalScheme.defaultBackground
    val mix = ColorUtil.mix(background, color, color.alpha / 255.0)

    return TextAttributes.fromFlyweight(
      attributes.flyweight
        .withBackground(mix)
        .withForeground(if (ColorUtil.isDark(mix)) Gray._254 else Gray._1))
  }

  private fun getHighlightInfoBuilder(color: Color): HighlightInfo.Builder {
    var newHighlightInfo = HighlightInfo.newHighlightInfo(COLOR_ELEMENT)
      .textAttributes(getAttributesFlyweight(color))

    if (GutterColorLineMarkerProvider.isEnabled()) {
      newHighlightInfo = newHighlightInfo.gutterIconRenderer(GutterColorRenderer(color))
    }

    return newHighlightInfo
  }

}
