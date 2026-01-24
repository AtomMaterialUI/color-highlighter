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

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.mallowigi.listeners.ColorHighlighterNotifier

@State(
  name = "Color Highlighter Settings",
  storages = [Storage("color-highlighter.xml")],
  category = SettingsCategory.UI
)
class ColorHighlighterState : SimplePersistentStateComponent<ColorHighlighterState.State>(State()) {

  class State : BaseState() {
    var isEnabled: Boolean by property(true)

    var isHexDetectEnabled: Boolean by property(true)

    var isColorNamesDetectEnabled: Boolean by property(true)

    var isTupleDetectEnabled: Boolean by property(true)

    var isRgbaEnabled: Boolean by property(true)

    var isJavaColorCtorEnabled: Boolean by property(true)

    var isJavaColorMethodEnabled: Boolean by property(true)

    var isKotlinColorCtorEnabled: Boolean by property(true)

    var isKotlinColorMethodEnabled: Boolean by property(true)

    var isScalaColorCtorEnabled: Boolean by property(true)

    var isScalaColorMethodEnabled: Boolean by property(true)

    var isGroovyColorCtorEnabled: Boolean by property(true)

    var isGroovyColorMethodEnabled: Boolean by property(true)

    var isRiderColorMethodEnabled: Boolean by property(true)

    var isRustColorCtorEnabled: Boolean by property(true)

    var isTextEnabled: Boolean by property(false)

    var isMarkdownEnabled: Boolean by property(true)

    var isMarkupEnabled: Boolean by property(true)

    var isCssEnabled: Boolean by property(true)

    var highlightingStyle: HighlightingStyles by enum(HighlightingStyles.BACKGROUND)

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

  var isColorNamesDetectEnabled: Boolean
    get() = state.isColorNamesDetectEnabled
    set(value) {
      state.isColorNamesDetectEnabled = value
    }

  var isTupleDetectEnabled: Boolean
    get() = state.isTupleDetectEnabled
    set(value) {
      state.isTupleDetectEnabled = value
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

  var isScalaColorCtorEnabled: Boolean
    get() = state.isScalaColorCtorEnabled
    set(value) {
      state.isScalaColorCtorEnabled = value
    }

  var isScalaColorMethodEnabled: Boolean
    get() = state.isScalaColorMethodEnabled
    set(value) {
      state.isScalaColorMethodEnabled = value
    }

  var isGroovyColorCtorEnabled: Boolean
    get() = state.isGroovyColorCtorEnabled
    set(value) {
      state.isGroovyColorCtorEnabled = value
    }

  var isGroovyColorMethodEnabled: Boolean
    get() = state.isGroovyColorMethodEnabled
    set(value) {
      state.isGroovyColorMethodEnabled = value
    }

  var isRiderColorMethodEnabled: Boolean
    get() = state.isRiderColorMethodEnabled
    set(value) {
      state.isRiderColorMethodEnabled = value
    }

  var isRustColorCtorEnabled: Boolean
    get() = state.isRustColorCtorEnabled
    set(value) {
      state.isRustColorCtorEnabled = value
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

  var isMarkupEnabled: Boolean
    get() = state.isMarkupEnabled
    set(value) {
      state.isMarkupEnabled = value
    }

  var isCssColorEnabled: Boolean
    get() = state.isCssEnabled
    set(value) {
      state.isCssEnabled = value
    }

  var highlightingStyle: HighlightingStyles
    get() = state.highlightingStyle
    set(value) {
      state.highlightingStyle = value
    }

  var version: String?
    get() = state.version
    set(value) {
      state.version = value
    }

  fun resetSettings() {
    this.highlightingStyle = HighlightingStyles.BACKGROUND
    this.isColorNamesDetectEnabled = true
    this.isCssColorEnabled = true
    this.isCssColorEnabled = true
    this.isEnabled = true
    this.isHexDetectEnabled = true
    this.isJavaColorCtorEnabled = true
    this.isJavaColorMethodEnabled = true
    this.isKotlinColorCtorEnabled = true
    this.isKotlinColorMethodEnabled = true
    this.isMarkdownEnabled = true
    this.isMarkupEnabled = true
    this.isRgbaEnabled = true
    this.isRiderColorMethodEnabled = true
    this.isRustColorCtorEnabled = true
    this.isTextEnabled = false
    this.isTupleDetectEnabled = true
    this.version = VERSION

    updateEditors()
    fireChanged()
  }

  private fun updateEditors() {
    ApplicationManager.getApplication().invokeLater {
      val openProjects: Array<Project> = ProjectManager.getInstance().openProjects
      for (project in openProjects) {
        DaemonCodeAnalyzer.getInstance(project)?.restart(this)
      }
    }
  }

  private fun fireChanged(): Unit = ApplicationManager.getApplication().messageBus
    .syncPublisher(ColorHighlighterNotifier.TOPIC)
    .configChanged(this)

  fun clone(): ColorHighlighterState {
    val clone = ColorHighlighterState()
    clone.highlightingStyle = this.highlightingStyle
    clone.isColorNamesDetectEnabled = this.isColorNamesDetectEnabled
    clone.isCssColorEnabled = this.isCssColorEnabled
    clone.isCssColorEnabled = this.isCssColorEnabled
    clone.isEnabled = this.isEnabled
    clone.isHexDetectEnabled = this.isHexDetectEnabled
    clone.isJavaColorCtorEnabled = this.isJavaColorCtorEnabled
    clone.isJavaColorMethodEnabled = this.isJavaColorMethodEnabled
    clone.isKotlinColorCtorEnabled = this.isKotlinColorCtorEnabled
    clone.isKotlinColorMethodEnabled = this.isKotlinColorMethodEnabled
    clone.isMarkdownEnabled = this.isMarkdownEnabled
    clone.isMarkupEnabled = this.isMarkupEnabled
    clone.isRgbaEnabled = this.isRgbaEnabled
    clone.isRiderColorMethodEnabled = this.isRiderColorMethodEnabled
    clone.isRustColorCtorEnabled = this.isRustColorCtorEnabled
    clone.isTextEnabled = this.isTextEnabled
    clone.isTupleDetectEnabled = this.isTupleDetectEnabled
    clone.version = this.version
    return clone
  }

  fun apply(state: ColorHighlighterState) {
    this.highlightingStyle = state.highlightingStyle
    this.isColorNamesDetectEnabled = state.isColorNamesDetectEnabled
    this.isCssColorEnabled = state.isCssColorEnabled
    this.isEnabled = state.isEnabled
    this.isHexDetectEnabled = state.isHexDetectEnabled
    this.isJavaColorCtorEnabled = state.isJavaColorCtorEnabled
    this.isJavaColorMethodEnabled = state.isJavaColorMethodEnabled
    this.isKotlinColorCtorEnabled = state.isKotlinColorCtorEnabled
    this.isKotlinColorMethodEnabled = state.isKotlinColorMethodEnabled
    this.isMarkdownEnabled = state.isMarkdownEnabled
    this.isMarkupEnabled = state.isMarkupEnabled
    this.isRgbaEnabled = state.isRgbaEnabled
    this.isRiderColorMethodEnabled = state.isRiderColorMethodEnabled
    this.isRustColorCtorEnabled = state.isRustColorCtorEnabled
    this.isTextEnabled = state.isTextEnabled
    this.isTupleDetectEnabled = state.isTupleDetectEnabled

    updateEditors()
    this.fireChanged()
  }

  /** Equals. */
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ColorHighlighterState

    if (highlightingStyle != other.highlightingStyle) return false
    if (isColorNamesDetectEnabled != other.isColorNamesDetectEnabled) return false
    if (isCssColorEnabled != other.isCssColorEnabled) return false
    if (isCssColorEnabled != other.isCssColorEnabled) return false
    if (isEnabled != other.isEnabled) return false
    if (isHexDetectEnabled != other.isHexDetectEnabled) return false
    if (isJavaColorCtorEnabled != other.isJavaColorCtorEnabled) return false
    if (isJavaColorMethodEnabled != other.isJavaColorMethodEnabled) return false
    if (isKotlinColorCtorEnabled != other.isKotlinColorCtorEnabled) return false
    if (isKotlinColorMethodEnabled != other.isKotlinColorMethodEnabled) return false
    if (isMarkdownEnabled != other.isMarkdownEnabled) return false
    if (isMarkupEnabled != other.isMarkupEnabled) return false
    if (isRgbaEnabled != other.isRgbaEnabled) return false
    if (isRiderColorMethodEnabled != other.isRiderColorMethodEnabled) return false
    if (isRustColorCtorEnabled != other.isRustColorCtorEnabled) return false
    if (isTextEnabled != other.isTextEnabled) return false
    if (isTupleDetectEnabled != other.isTupleDetectEnabled) return false
    if (version != other.version) return false
    return true
  }

  /** Hash code. */
  override fun hashCode(): Int {
    var result = isEnabled.hashCode()
    result = 31 * result + highlightingStyle.hashCode()
    result = 31 * result + isColorNamesDetectEnabled.hashCode()
    result = 31 * result + isCssColorEnabled.hashCode()
    result = 31 * result + isHexDetectEnabled.hashCode()
    result = 31 * result + isJavaColorCtorEnabled.hashCode()
    result = 31 * result + isJavaColorMethodEnabled.hashCode()
    result = 31 * result + isKotlinColorCtorEnabled.hashCode()
    result = 31 * result + isKotlinColorMethodEnabled.hashCode()
    result = 31 * result + isMarkdownEnabled.hashCode()
    result = 31 * result + isMarkupEnabled.hashCode()
    result = 31 * result + isRgbaEnabled.hashCode()
    result = 31 * result + isRiderColorMethodEnabled.hashCode()
    result = 31 * result + isRustColorCtorEnabled.hashCode()
    result = 31 * result + isTextEnabled.hashCode()
    result = 31 * result + isTupleDetectEnabled.hashCode()
    result = 31 * result + version.hashCode()
    return result
  }

  companion object {
    @JvmStatic
    val instance: ColorHighlighterState by lazy { service() }

    const val VERSION = "20.0.0"
  }
}