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

package com.mallowigi.config.home

import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.selected
import com.mallowigi.ColorHighlighterBundle.message
import com.mallowigi.ColorHighlighterIcons.Settings.ANDROID_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.COLOR_NAMES_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.CSS_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.HEX_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.JAVA_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.KOTLIN_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.MAIN_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.MARKDOWN_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.MARKUP_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.RUST_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.SCALA_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.TEXT_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.TUPLE_ICON
import com.mallowigi.ColorHighlighterIcons.Settings.XCODE_ICON
import com.mallowigi.FeatureLoader
import com.mallowigi.inlay.refreshInlayHints
import org.jetbrains.annotations.NonNls
import javax.swing.DefaultComboBoxModel
import javax.swing.Icon
import javax.swing.JList

class ColorHighlighterConfigurable : BoundSearchableConfigurable(
  message("ColorHighlighterForm.title"),
  "com.mallowigi.config.home.ColorHighlighterConfigurable",
) {
  private lateinit var main: DialogPanel
  private lateinit var javaPanel: CollapsibleRow
  private lateinit var kotlinPanel: CollapsibleRow
  private lateinit var scalaPanel: CollapsibleRow
  private lateinit var rustPanel: CollapsibleRow
  private lateinit var markdownPanel: CollapsibleRow
  private lateinit var markupPanel: CollapsibleRow
  private lateinit var textPanel: CollapsibleRow
  private lateinit var riderPanel: CollapsibleRow
  private val settings = ColorHighlighterState.instance
  private val settingsClone = ColorHighlighterState.instance.clone()

  override fun getId(): String = ID

  @Suppress("Detekt.LongMethod")
  private fun initComponents() {
    lateinit var enabledCheckbox: JBCheckBox
    val arrowsRenderer = arrowsRenderer()


    main = panel {
      group(message("ColorHighlighterSettingsForm.globalSeparator.text")) {
        row {
          icon(MAIN_ICON)
            .gap(RightGap.SMALL)
          enabledCheckbox = checkBox(message("ColorHighlighterSettingsForm.enableCheckbox.text"))
            .bindSelected(settingsClone::isEnabled)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.enableCheckbox.toolTipText"))

        row {
          icon(HEX_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorParsingCheckbox.text"))
            .bindSelected(settingsClone::isHexDetectEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorParsingCheckbox.toolTipText"))

        row {
          icon(COLOR_NAMES_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorNamesParsingCheckbox.text"))
            .bindSelected(settingsClone::isColorNamesDetectEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorNamesParsingCheckbox.toolTipText"))

        row {
          icon(TUPLE_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.tupleDetectCheckbox.text"))
            .bindSelected(settingsClone::isTupleDetectEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.tupleDetectCheckbox.toolTipText"))


        row {
          icon(ANDROID_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.rgbaCheckbox.text"))
            .bindSelected(settingsClone::isRgbaEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.rgbaCheckbox.toolTipText"))

        row {
          icon(CSS_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.cssCheckbox.text"))
            .bindSelected(settingsClone::isCssColorEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.cssCheckbox.toolTipText"))

        row(message("ColorHighlighterSettingsForm.highlightingStyle.text")) {
          val model = DefaultComboBoxModel(HighlightingStyles.values())
          comboBox(model, arrowsRenderer)
            .bindItem(settingsClone::highlightingStyle) {
              settingsClone.highlightingStyle = it ?: HighlightingStyles.BACKGROUND
            }
        }.rowComment(message("ColorHighlighterSettingsForm.highlightingStyle.toolTipText"))
      }

      markdownPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.markdownSeparator.text")) {
        row {
          icon(MARKDOWN_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.markdownCheckbox.text"))
            .bindSelected(settingsClone::isMarkdownEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.markdownCheckbox.toolTipText"))
      }

      markupPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.markupSeparator.text")) {
        row {
          icon(MARKUP_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.markupCheckbox.text"))
            .bindSelected(settingsClone::isMarkupEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.markupCheckbox.toolTipText"))
      }

      javaPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.javaSeparator.text")) {
        row {
          icon(JAVA_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorCtorCheckbox.text"))
            .bindSelected(settingsClone::isJavaColorCtorEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorCtorCheckbox.toolTipText"))

        row {
          icon(JAVA_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorMethodCheckbox.text"))
            .bindSelected(settingsClone::isJavaColorMethodEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorMethodCheckbox.toolTipText"))
      }

      kotlinPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.kotlinSeparator.text")) {
        row {
          icon(KOTLIN_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorKtCtorCheckbox.text"))
            .bindSelected(settingsClone::isKotlinColorCtorEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorKtCtorCheckbox.toolTipText"))

        row {
          icon(KOTLIN_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorKtMethodCheckbox.text"))
            .bindSelected(settingsClone::isKotlinColorMethodEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorKtMethodCheckbox.toolTipText"))
      }

      scalaPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.scalaSeparator.text")) {
        row {
          icon(SCALA_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorKtCtorCheckbox.text"))
            .bindSelected(settingsClone::isScalaColorCtorEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorKtCtorCheckbox.toolTipText"))

        row {
          icon(SCALA_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorKtMethodCheckbox.text"))
            .bindSelected(settingsClone::isScalaColorMethodEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorKtMethodCheckbox.toolTipText"))
      }

      rustPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.rustSeparator.text")) {
        row {
          icon(RUST_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.colorRustCheckbox.text"))
            .bindSelected(settingsClone::isRustColorCtorEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.colorRustCheckbox.toolTipText"))

      }

      riderPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.riderSeparator.text")) {
        row {
          icon(XCODE_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.riderColorMethodCheckbox.text"))
            .bindSelected(settingsClone::isRiderColorMethodEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.riderColorMethodCheckbox.toolTipText"))
      }

      textPanel = collapsibleGroup(message("ColorHighlighterSettingsForm.textPanelSeparator.text")) {
        row {
          icon(TEXT_ICON)
            .gap(RightGap.SMALL)
          checkBox(message("ColorHighlighterSettingsForm.textCheckbox.text"))
            .bindSelected(settingsClone::isTextEnabled)
            .enabledIf(enabledCheckbox.selected)
            .gap(RightGap.SMALL)
            .component
        }.rowComment(message("ColorHighlighterSettingsForm.textCheckbox.toolTipText"))
      }

      row {
        button(message("ColorHighlighterSettingsForm.resetDefaultsButton.text")) { resetSettings() }
          .resizableColumn()
          .align(AlignX.RIGHT)
      }
    }
  }

  override fun createPanel(): DialogPanel {
    initComponents()
    toggleFeatures()
    return main
  }

  override fun isModified(): Boolean {
    if (super.isModified()) return true
    return settings != settingsClone
  }

  private fun resetSettings() {
    if (Messages.showOkCancelDialog(
        message("ColorHighlighterSettingsForm.resetDefaultsButton.confirmation"),
        message("ColorHighlighterSettingsForm.resetDefaultsButton.confirmation.title"),
        message("ColorHighlighterSettingsForm.resetDefaultsButton.confirmation.ok"),
        message("ColorHighlighterSettingsForm.resetDefaultsButton.confirmation.cancel"),
        Messages.getQuestionIcon(),
      ) == Messages.OK
    ) {
      settingsClone.resetSettings()
      main.reset()
    }
  }

  override fun apply() {
    super.apply()
    settings.apply(settingsClone)
    refreshInlayHints()
  }

  private fun toggleFeatures() {
    val featureLoader = FeatureLoader.instance
    if (!featureLoader.isJavaEnabled) {
      javaPanel.visible(false)
    }
    if (!featureLoader.isKotlinEnabled) {
      kotlinPanel.visible(false)
    }
    if (!featureLoader.isRiderEnabled) {
      riderPanel.visible(false)
    }
    if (!featureLoader.isScalaEnabled) {
      scalaPanel.visible(false)
    }
    if (!featureLoader.isMarkdownEnabled) {
      markdownPanel.visible(false)
    }
    if (!featureLoader.isRustEnabled) {
      rustPanel.visible(false)
    }
  }

  private fun arrowsRenderer(): SimpleListCellRenderer<HighlightingStyles?> {
    val renderer = object : SimpleListCellRenderer<HighlightingStyles?>() {
      override fun customize(
        list: JList<out HighlightingStyles?>,
        value: HighlightingStyles?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean,
      ) {
        if (value == null) return
        val baseIcon: Icon = value.icon
        icon = baseIcon
        text = value.type
      }
    }
    return renderer
  }

  companion object {
    @NonNls
    const val ID = "com.mallowigi.config.home.ColorHighlighterConfigurable"
  }
}

