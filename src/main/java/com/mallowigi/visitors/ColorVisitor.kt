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
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.mallowigi.config.home.ColorHighlighterState.Companion.instance
import com.mallowigi.config.home.HighlightingStyles
import com.mallowigi.highlighters.RoundedBackgroundPainter
import com.mallowigi.highlighters.RoundedHighlight
import com.mallowigi.highlighters.RoundedPaintStyle
import com.mallowigi.search.ColorMatch
import com.mallowigi.search.parsers.ColorParser
import java.awt.Color

/** Color visitor: This is the class that will colorize the texts representing colors. */
abstract class ColorVisitor : HighlightVisitor, LangVisitor, DumbAware {

  private var highlightInfoHolder: HighlightInfoHolder? = null
  private val roundedHighlights = mutableListOf<RoundedHighlight>()
  internal val config = instance
  private val roundedStyles = setOf(HighlightingStyles.BACKGROUND, HighlightingStyles.BORDER, HighlightingStyles.UNDERLINE_PILL)

  /**
   * Highlight the element with the given color.
   *
   * @param element element representing a color
   * @param color color
   */
  fun highlight(element: PsiElement?, color: Color) {
    if (!instance.isEnabled) return

    val textRange = element?.textRange
    val style = instance.highlightingStyle
    if (style in roundedStyles && textRange != null) {
      roundedHighlights += RoundedHighlight(
        range = IntRange(textRange.startOffset, textRange.endOffset),
        color = color,
        paintStyle = style.toRoundedPaintStyle()
      )
    }

    assert(highlightInfoHolder != null)
    highlightInfoHolder!!.add(ColorHighlighter.highlightColor(element, color))
  }

  fun highlight(color: Color, range: IntRange) {
    if (!instance.isEnabled) return
    val style = instance.highlightingStyle
    if (style in roundedStyles) {
      roundedHighlights += RoundedHighlight(
        range = range,
        color = color,
        paintStyle = style.toRoundedPaintStyle()
      )
    }
    assert(highlightInfoHolder != null)
    highlightInfoHolder!!.add(ColorHighlighter.highlightColor(range, color))
  }

  /**
   * Analyze: runs the annotate action on the file.
   *
   * @param file the file to annotate
   * @param updateWholeFile whether to update the whole file
   * @param holder highlighting info holder
   * @param action action to run
   * @return
   */
  override fun analyze(
    file: PsiFile,
    updateWholeFile: Boolean,
    holder: HighlightInfoHolder,
    action: Runnable
  ): Boolean {
    highlightInfoHolder = holder
    roundedHighlights.clear()
    val visitorKey = this::class.qualifiedName ?: this::class.java.name

    try {
      action.run()
    } finally {
      val style = instance.highlightingStyle
      if (instance.isEnabled && style in roundedStyles) {
        RoundedBackgroundPainter.apply(
          file = file,
          visitorKey = visitorKey,
          highlights = roundedHighlights,
          arcRadius = instance.roundedArcRadius
        )
      } else {
        RoundedBackgroundPainter.clear(
          file = file,
          visitorKey = visitorKey
        )
      }
      roundedHighlights.clear()
      highlightInfoHolder = null
    }
    return true
  }

  override fun visit(element: PsiElement) {
    when {
      this.canAcceptMultiple() -> {
        val colors = this.acceptMultiple(element)
        colors?.forEach { match ->
          val absoluteRange = IntRange(
            element.textRange.startOffset + match.range.first,
            element.textRange.startOffset + match.range.last
          )
          highlight(match.color, absoluteRange)
        }
      }

      else                     -> {
        val color = this.accept(element)
        color?.let { highlight(element, it) }
      }
    }
  }

  /**
   * Clones the visitor. This method is mandatory.
   *
   * @return instance of HighlightVisitor
   */
  abstract override fun clone(): HighlightVisitor

  override fun getParser(text: String): ColorParser? = null

  override fun shouldParseText(text: String): Boolean = false

  override fun shouldVisit(): Boolean = true

  override fun accept(element: PsiElement): Color? = null

  override fun canAcceptMultiple(): Boolean = false

  override fun acceptMultiple(element: PsiElement): List<ColorMatch>? = null

  private fun HighlightingStyles.toRoundedPaintStyle(): RoundedPaintStyle = when (this) {
    HighlightingStyles.BACKGROUND -> RoundedPaintStyle.BACKGROUND
    HighlightingStyles.BORDER -> RoundedPaintStyle.BORDER
    HighlightingStyles.UNDERLINE_PILL -> RoundedPaintStyle.UNDERLINE_PILL
    else -> RoundedPaintStyle.BACKGROUND
  }
}
