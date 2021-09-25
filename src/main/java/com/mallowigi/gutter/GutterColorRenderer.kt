/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
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
package com.mallowigi.gutter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.ui.ColorChooser
import com.intellij.ui.ColorUtil
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.EmptyIcon
import com.mallowigi.ColorHighlighterBundle.message
import org.jetbrains.annotations.NonNls
import java.awt.Color
import java.awt.datatransfer.StringSelection
import java.util.*
import javax.swing.Icon

class GutterColorRenderer(private val color: Color?) : GutterIconRenderer() {
  override fun getIcon(): Icon = when {
    color != null -> JBUIScale.scaleIcon(ColorIcon(ICON_SIZE, color))
    else -> JBUIScale.scaleIcon(EmptyIcon.create(ICON_SIZE))
  }

  override fun getTooltipText(): String = message("choose.color")

  override fun getClickAction(): @NonNls AnAction {
    return object : AnAction(message("choose.color1")) {
      override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val currentColor = color ?: return
        val newColor = ColorChooser.chooseColor(
          editor.project,
          editor.component,
          message("replace.color"),
          currentColor,
          false
        )
        copyColor(currentColor, newColor)
      }

      private fun copyColor(currentColor: Color, newColor: Color?) {
        if (newColor == null || newColor == currentColor) return

        CopyPasteManager.getInstance().setContents(StringSelection(ColorUtil.toHex(newColor, false)))
      }
    }
  }

  override fun isNavigateAction(): Boolean = true

  override fun equals(other: Any?): Boolean {
    return when {
      this === other -> true
      other == null || javaClass != other.javaClass -> false
      else -> {
        val renderer = other as GutterColorRenderer
        color == renderer.color
      }
    }
  }

  override fun hashCode(): Int = Objects.hash(color)

  override fun getAlignment(): Alignment = Alignment.RIGHT

  companion object {
    private const val ICON_SIZE = 12
  }
}
