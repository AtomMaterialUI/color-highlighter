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
package com.mallowigi.config.ui.columns

import com.intellij.openapi.Disposable
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.ui.cellvalidators.StatefulValidatingCellEditor
import com.intellij.openapi.ui.cellvalidators.ValidatingTableCellRendererWrapper
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo
import com.mallowigi.ColorHighlighterBundle
import com.mallowigi.colors.SingleColor
import com.mallowigi.utils.ColorUtils
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

/**
 * Editable column info for association name
 *
 * @property parent the Parent class
 */
@Suppress("UnstableApiUsage")
class ColorEditableColumnInfo(private val parent: Disposable) :
  EditableColumnInfo<SingleColor?, String>(ColorHighlighterBundle.message("CustomColorsForm.columns.color")) {
  override fun valueOf(item: SingleColor?): String? = item?.code

  override fun setValue(item: SingleColor?, value: String?) {
    item?.code = value!!
  }

  override fun getEditor(item: SingleColor?): TableCellEditor {
    val cellEditor = ExtendableTextField()
    return StatefulValidatingCellEditor(cellEditor, parent)
  }

  override fun getRenderer(item: SingleColor?): TableCellRenderer? {
    return ValidatingTableCellRendererWrapper(object : DefaultTableCellRenderer() {
      override fun repaint() {
        if (item?.code != null) {
          val bgColor = ColorUtils.getHex(item.code)
          background = bgColor
          foreground = ColorUtils.getSuitableForeground(bgColor)
        }
      }
    })
      .withCellValidator { value: Any?, _: Int, _: Int ->
        if (value == null || value == "") {
          return@withCellValidator ValidationInfo(ColorHighlighterBundle.message("CustomColorsForm.ColorEditor.empty"))
        } else {
          return@withCellValidator null
        }
      }
  }

  override fun isCellEditable(item: SingleColor?): Boolean = false
}

