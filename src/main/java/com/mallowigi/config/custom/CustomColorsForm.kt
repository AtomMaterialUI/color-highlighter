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
package com.mallowigi.config.custom

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.ColumnInfo
import com.mallowigi.ColorHighlighterBundle.message
import com.mallowigi.colors.CustomColors
import com.mallowigi.colors.SingleColor
import com.mallowigi.config.SettingsFormUI
import com.mallowigi.config.ui.columns.ColorEditableColumnInfo
import com.mallowigi.config.ui.columns.NameEditableColumnInfo
import com.mallowigi.config.ui.internal.CustomColorsTableItemEditor
import com.mallowigi.config.ui.internal.CustomColorsTableModelEditor
import javax.swing.JComponent
import javax.swing.UIManager

/** Settings form used to edit the user defined custom colors. */
class CustomColorsForm :
  SettingsFormUI<CustomColorsForm, CustomColorsConfig>,
  Disposable {

  private val columns: Array<ColumnInfo<*, *>> = arrayOf(
    NameEditableColumnInfo(this, true),
    ColorEditableColumnInfo(this),
  )

  private var customColorsEditor: CustomColorsTableModelEditor<SingleColor>? = null
  private lateinit var mainPanel: DialogPanel

  override val content: JComponent
    get() = mainPanel

  override fun init() {
    val editor = CustomColorsTableModelEditor(
      columns,
      CustomColorsTableItemEditor(),
      message("no.custom.colors"),
    )
    customColorsEditor = editor

    mainPanel = panel {
      group("Custom Colors Editor") {
        row {
          label(message("CustomColorsForm.explanation.text"))
            .applyToComponent {
              font = font.deriveFont(font.size - 1f)
              foreground = UIManager.getColor("inactiveCaptionText")
            }
        }
        row {
          cell(editor.createComponent()).align(Align.FILL)
        }.resizableRow()
      }
    }
  }

  override fun afterStateSet() {
    // add after state set
  }

  override fun dispose() {
    customColorsEditor = null
  }

  override fun setFormState(config: CustomColorsConfig) {
    ApplicationManager.getApplication().invokeLater {
      customColorsEditor?.reset(config.customColors.getTheAssociations())
      afterStateSet()
    }
  }

  override fun isModified(config: CustomColorsConfig): Boolean =
    customColorsEditor?.let { config.isCustomColorsModified(it.getModel().items) } ?: false

  val customColors: CustomColors
    get() = CustomColors(customColorsEditor!!.getModel().items)
}
