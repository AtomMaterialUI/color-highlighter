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

package com.mallowigi.config.home

import com.intellij.application.options.editor.WebEditorOptions
import com.intellij.ide.ui.LafManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.mallowigi.listeners.ColorHighlighterNotifier

/**
 * Represents the state of the Color Highlighter plugin settings.
 * The settings are persisted in the "color-highlighter.xml" file.
 *
 * @property isEnabled Indicates whether the Color Highlighter plugin is enabled.
 * @property isHexDetectEnabled Indicates whether Hex color detection is enabled.
 * @property isRgbaEnabled Indicates whether RGBA color detection is enabled.
 * @property isJavaColorCtorEnabled Indicates whether Java Color constructor color detection is enabled.
 * @property isJavaColorMethodEnabled Indicates whether Java Color method color detection is enabled.
 * @property isKotlinColorCtorEnabled Indicates whether Kotlin Color constructor color detection is enabled.
 * @property isKotlinColorMethodEnabled Indicates whether Kotlin Color method color detection is enabled.
 * @property isRiderColorMethodEnabled Indicates whether Rider Color method color detection is enabled.
 * @property isTextEnabled Indicates whether color highlighting in plain text files is enabled.
 * @property isMarkdownEnabled Indicates whether color highlighting in Markdown files is enabled.
 * @property isCssColorEnabled Indicates whether CSS inline color preview is enabled.
 * @property version The version string of the Color Highlighter plugin.
 */
@State(
  name = "Color Highlighter Settings",
  storages = [Storage("color-highlighter.xml")],
  category = SettingsCategory.UI
)
class ColorHighlighterState : SimplePersistentStateComponent<ColorHighlighterState.State>(State()) {

  class State : BaseState() {
    var isEnabled: Boolean by property(true)

    var isHexDetectEnabled: Boolean by property(true)

    var isRgbaEnabled: Boolean by property(true)

    var isJavaColorCtorEnabled: Boolean by property(true)

    var isJavaColorMethodEnabled: Boolean by property(true)

    var isKotlinColorCtorEnabled: Boolean by property(true)

    var isKotlinColorMethodEnabled: Boolean by property(true)

    var isRiderColorMethodEnabled: Boolean by property(true)

    var isTextEnabled: Boolean by property(false)

    var isMarkdownEnabled: Boolean by property(true)

    var version: String? by string(VERSION)
  }

  var isEnabled: Boolean
    get() = state.isEnabled
    set(value) {
      state.isEnabled = value
    }

  var isHexDetectEnabled: Boolean
    get() = state.isHexDetectEnabled
    set(value) {
      state.isHexDetectEnabled = value
    }

  var isRgbaEnabled: Boolean
    get() = state.isRgbaEnabled
    set(value) {
      state.isRgbaEnabled = value
    }

  var isJavaColorCtorEnabled: Boolean
    get() = state.isJavaColorCtorEnabled
    set(value) {
      state.isJavaColorCtorEnabled = value
    }

  var isJavaColorMethodEnabled: Boolean
    get() = state.isJavaColorMethodEnabled
    set(value) {
      state.isJavaColorMethodEnabled = value
    }

  var isKotlinColorCtorEnabled: Boolean
    get() = state.isKotlinColorCtorEnabled
    set(value) {
      state.isKotlinColorCtorEnabled = value
    }

  var isKotlinColorMethodEnabled: Boolean
    get() = state.isKotlinColorMethodEnabled
    set(value) {
      state.isKotlinColorMethodEnabled = value
    }

  var isRiderColorMethodEnabled: Boolean
    get() = state.isRiderColorMethodEnabled
    set(value) {
      state.isRiderColorMethodEnabled = value
    }

  var isTextEnabled: Boolean
    get() = state.isTextEnabled
    set(value) {
      state.isTextEnabled = value
    }

  var isMarkdownEnabled: Boolean
    get() = state.isMarkdownEnabled
    set(value) {
      state.isMarkdownEnabled = value
    }

  var isCssColorEnabled: Boolean
    get() = WebEditorOptions.getInstance().isShowCssInlineColorPreview
    set(value) {
      WebEditorOptions.getInstance().isShowCssInlineColorPreview = value
    }

  var version: String?
    get() = state.version
    set(value) {
      state.version = value
    }

  fun resetSettings() {
    this.isEnabled = true
    this.isHexDetectEnabled = true
    this.isRgbaEnabled = true
    this.isJavaColorCtorEnabled = true
    this.isJavaColorMethodEnabled = true
    this.isKotlinColorCtorEnabled = true
    this.isKotlinColorMethodEnabled = true
    this.isRiderColorMethodEnabled = true
    this.isCssColorEnabled = true
    this.isTextEnabled = false
    this.isMarkdownEnabled = true
    this.version = VERSION

    ApplicationManager.getApplication().invokeAndWait { LafManager.getInstance().updateUI() }
    fireChanged()
  }

  private fun fireChanged(): Unit = ApplicationManager.getApplication().messageBus
    .syncPublisher(ColorHighlighterNotifier.TOPIC)
    .configChanged(this)

  fun clone(): ColorHighlighterState {
    val clone = ColorHighlighterState()
    clone.isEnabled = this.isEnabled
    clone.isHexDetectEnabled = this.isHexDetectEnabled
    clone.isRgbaEnabled = this.isRgbaEnabled
    clone.isJavaColorCtorEnabled = this.isJavaColorCtorEnabled
    clone.isJavaColorMethodEnabled = this.isJavaColorMethodEnabled
    clone.isKotlinColorCtorEnabled = this.isKotlinColorCtorEnabled
    clone.isKotlinColorMethodEnabled = this.isKotlinColorMethodEnabled
    clone.isRiderColorMethodEnabled = this.isRiderColorMethodEnabled
    clone.isTextEnabled = this.isTextEnabled
    clone.isMarkdownEnabled = this.isMarkdownEnabled
    clone.isCssColorEnabled = this.isCssColorEnabled
    clone.version = this.version
    return clone
  }

  fun apply(state: ColorHighlighterState) {
    this.isEnabled = state.isEnabled
    this.isHexDetectEnabled = state.isHexDetectEnabled
    this.isRgbaEnabled = state.isRgbaEnabled
    this.isJavaColorCtorEnabled = state.isJavaColorCtorEnabled
    this.isJavaColorMethodEnabled = state.isJavaColorMethodEnabled
    this.isKotlinColorCtorEnabled = state.isKotlinColorCtorEnabled
    this.isKotlinColorMethodEnabled = state.isKotlinColorMethodEnabled
    this.isRiderColorMethodEnabled = state.isRiderColorMethodEnabled
    this.isTextEnabled = state.isTextEnabled
    this.isMarkdownEnabled = state.isMarkdownEnabled
    this.isCssColorEnabled = state.isCssColorEnabled

    ApplicationManager.getApplication().invokeAndWait { LafManager.getInstance().updateUI() }
    this.fireChanged()
  }

  /** Equals. */
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ColorHighlighterState

    if (isEnabled != other.isEnabled) return false
    if (isHexDetectEnabled != other.isHexDetectEnabled) return false
    if (isRgbaEnabled != other.isRgbaEnabled) return false
    if (isJavaColorCtorEnabled != other.isJavaColorCtorEnabled) return false
    if (isJavaColorMethodEnabled != other.isJavaColorMethodEnabled) return false
    if (isKotlinColorCtorEnabled != other.isKotlinColorCtorEnabled) return false
    if (isKotlinColorMethodEnabled != other.isKotlinColorMethodEnabled) return false
    if (isRiderColorMethodEnabled != other.isRiderColorMethodEnabled) return false
    if (isTextEnabled != other.isTextEnabled) return false
    if (isMarkdownEnabled != other.isMarkdownEnabled) return false
    if (isCssColorEnabled != other.isCssColorEnabled) return false
    if (version != other.version) return false
    return true
  }

  /** Hash code. */
  override fun hashCode(): Int {
    var result = isEnabled.hashCode()
    result = 31 * result + isHexDetectEnabled.hashCode()
    result = 31 * result + isRgbaEnabled.hashCode()
    result = 31 * result + isJavaColorCtorEnabled.hashCode()
    result = 31 * result + isJavaColorMethodEnabled.hashCode()
    result = 31 * result + isKotlinColorCtorEnabled.hashCode()
    result = 31 * result + isKotlinColorMethodEnabled.hashCode()
    result = 31 * result + isRiderColorMethodEnabled.hashCode()
    result = 31 * result + isTextEnabled.hashCode()
    result = 31 * result + isMarkdownEnabled.hashCode()
    result = 31 * result + version.hashCode()
    return result
  }

  companion object {
    @JvmStatic
    val instance: ColorHighlighterState by lazy { service() }

    const val VERSION = "8.0.0"
  }
}
