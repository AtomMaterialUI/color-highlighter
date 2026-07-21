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

import com.mallowigi.config.home.ColorHighlighterState
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.markup.CustomHighlighterRenderer
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.ui.ColorUtil
import com.mallowigi.config.home.ColorHighlighterState.Companion.MAX_ROUNDED_ARC_RADIUS
import com.mallowigi.config.home.ColorHighlighterState.Companion.MIN_ROUNDED_ARC_RADIUS
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import kotlin.math.max
import kotlin.math.min

data class RoundedHighlight(
  val range: IntRange,
  val color: Color,
  val paintStyle: RoundedPaintStyle
)

enum class RoundedPaintStyle {
  BACKGROUND,
  BORDER,
  UNDERLINE_PILL,
  GLOW,
}

object RoundedBackgroundPainter {
  private val editorHighlightsKey =
    Key.create<MutableMap<String, MutableList<RangeHighlighter>>>("color.highlighter.rounded.background")

  /**
   * Applies the highlights to the editors
   */
  fun apply(file: PsiFile, visitorKey: String, highlights: List<RoundedHighlight>, arcRadius: Int) {
    val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return
    val editors = EditorFactory.getInstance().allEditors.filter { editor ->
      editor.project == file.project && editor.document == document
    }

    editors.forEach { editor ->
      // First we remove all highlighters
      val highlightersMap = editor.getUserData(editorHighlightsKey) ?: mutableMapOf()
      val highlighters = highlightersMap.remove(visitorKey) ?: mutableListOf()
      val markupModel = editor.markupModel

      highlighters.forEach { highlighter ->
        markupModel.removeHighlighter(highlighter)
      }

      // Then we add the new ones
      val newHighlighters = mutableListOf<RangeHighlighter>()

      highlights.forEach { highlight ->
        val highlighter = markupModel.addRangeHighlighter(
          /* startOffset = */ highlight.range.first,
          /* endOffset = */ highlight.range.last,
          /* layer = */ HighlighterLayer.SELECTION - 1,
          /* textAttributes = */ null,
          /* targetArea = */ HighlighterTargetArea.EXACT_RANGE
        )
        // Install our custom renderer
        highlighter.customRenderer = RoundedRangeRenderer(
          color = highlight.color,
          paintStyle = highlight.paintStyle,
          arcRadius = arcRadius
        )
        newHighlighters.add(highlighter)
      }

      // Store the highlighters for later removal
      highlightersMap[visitorKey] = newHighlighters
      editor.putUserData(editorHighlightsKey, highlightersMap)
    }
  }

  /**
   * Clears all highlighters for the given visitor key
   */
  fun clear(file: PsiFile, visitorKey: String) {
    val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return
    val editors = EditorFactory.getInstance().allEditors.filter { editor ->
      editor.project == file.project && editor.document == document
    }

    editors.forEach { editor ->
      val highlightersMap = editor.getUserData(editorHighlightsKey) ?: return@forEach
      val highlighters = highlightersMap.remove(visitorKey) ?: return@forEach

      val markupModel = editor.markupModel
      highlighters.forEach { highlighter ->
        markupModel.removeHighlighter(highlighter)
      }
    }
  }
}

private class RoundedRangeRenderer(
  private val color: Color,
  private val paintStyle: RoundedPaintStyle,
  arcRadius: Int
) : CustomHighlighterRenderer {
  private val safeArcRadius = arcRadius.coerceIn(MIN_ROUNDED_ARC_RADIUS, MAX_ROUNDED_ARC_RADIUS)
  private val painter = StylePainterFactory.getPainter(paintStyle)

  override fun paint(editor: Editor, highlighter: RangeHighlighter, g: Graphics) {
    if (highlighter.startOffset >= highlighter.endOffset) return

    val g2 = g.create() as? Graphics2D ?: return

    try {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

      val mixedColor = ColorUtil.mix(
        /* c1 = */ editor.colorsScheme.defaultBackground,
        /* c2 = */ color,
        /* balance = */ color.alpha / 255.0
      )
      val lineHeight = editor.lineHeight

      val startLine = editor.document.getLineNumber(highlighter.startOffset)
      val endLine = editor.document.getLineNumber(highlighter.endOffset)

      for (line in startLine..endLine) {
        val lineStartOffset = when (line) {
          startLine -> highlighter.startOffset
          else      -> editor.document.getLineStartOffset(line)
        }
        val lineEndOffset = when (line) {
          endLine -> highlighter.endOffset
          else    -> editor.document.getLineEndOffset(line)
        }
        if (lineStartOffset >= lineEndOffset) continue

        // Compute the coordinates from the offsets
        val startPoint = editor.logicalPositionToXY(editor.offsetToLogicalPosition(lineStartOffset))
        val endPoint = editor.logicalPositionToXY(editor.offsetToLogicalPosition(lineEndOffset))
        val x = min(startPoint.x, endPoint.x)
        val width = max(1, endPoint.x - x)
        val y = startPoint.y + 1
        val height = max(1, lineHeight - 2)
        val arc = min(height, safeArcRadius * 2)

        painter.paint(g2, x, y, width, height, arc, color, mixedColor)
      }
    } finally {
      g2.dispose()
    }
  }
}
