package com.mallowigi.highlighters

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
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import kotlin.math.max
import kotlin.math.min

data class RoundedHighlight(val range: IntRange, val color: Color)

object RoundedBackgroundPainter {
  private val editorHighlightsKey =
    Key.create<MutableMap<String, MutableList<RangeHighlighter>>>("color.highlighter.rounded.background")

  /**
   * Applies the highlights to the editors
   */
  fun apply(file: PsiFile, visitorKey: String, highlights: List<RoundedHighlight>) {
    val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return
    val editors = EditorFactory.getInstance().allEditors.filter { editor ->
      editor.project == file.project && editor.document == document
    }

    editors.forEach { editor ->
      // First we remove all highlighters
      clear(editor, visitorKey)

      if (highlights.isEmpty()) {
        editor.contentComponent.repaint()
        return@forEach
      }

      // Then we recreate the highlighters, avoiding duplicates
      val created = highlights
        .distinctBy { "${it.range.first}:${it.range.last}:${it.color.rgb}" }
        .mapNotNull { addHighlighter(editor, it) }
        .toMutableList()

      // Finally we save the highlighters in the editor using the key. This will allow us to remove them later when the file is re-annotated.
      // The editors are stored in a map whose key is the visitor's class name,
      // so that each visitor can manage its own highlighters independently.
      if (created.isNotEmpty()) {
        val all = editor.getUserData(editorHighlightsKey) ?: mutableMapOf()
        all[visitorKey] = created
        editor.putUserData(editorHighlightsKey, all)
      }
      editor.contentComponent.repaint()
    }
  }

  fun clear(file: PsiFile, visitorKey: String) {
    val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return
    EditorFactory.getInstance().allEditors
      .filter { editor -> editor.project == file.project && editor.document == document }
      .forEach { editor -> clear(editor, visitorKey) }
  }

  /**
   * Clears the editor's saved highlighters for the given visitor key.
   */
  private fun clear(editor: Editor, visitorKey: String) {
    val all = editor.getUserData(editorHighlightsKey) ?: return

    all.remove(visitorKey)?.forEach { highlighter ->
      if (highlighter.isValid) {
        editor.markupModel.removeHighlighter(highlighter)
      }
    }

    if (all.isEmpty()) {
      editor.putUserData(editorHighlightsKey, null)
    }
  }

  /**
   * Adds a range highlighter and give it the custom renderer to use rounded backgrounds
   */
  private fun addHighlighter(editor: Editor, highlight: RoundedHighlight): RangeHighlighter? {
    val start = highlight.range.first
    val end = when {
      highlight.range.last > start -> highlight.range.last
      else                         -> start + 1
    }

    if (start !in 0..<end || end > editor.document.textLength) return null

    val rangeHighlighter = editor.markupModel.addRangeHighlighter(
      start,
      end,
      HighlighterLayer.SELECTION - 1,
      null,
      HighlighterTargetArea.EXACT_RANGE
    )
    rangeHighlighter.customRenderer = RoundedRangeRenderer(highlight.color)
    return rangeHighlighter
  }
}

/**
 * A renderer that highlights a range in the editor with rounded borders.
 */
private class RoundedRangeRenderer(private val color: Color) : CustomHighlighterRenderer {
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
      // computes an arc proportional to the line height, so that they appear rounded
      val arc = max(4, (lineHeight * 0.65).toInt())

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

        g2.color = mixedColor
        g2.fillRoundRect(x, y, width, height, arc, arc)
        g2.color = color
        g2.drawRoundRect(x, y, width, height, arc, arc)
      }
    } finally {
      g2.dispose()
    }
  }
}
