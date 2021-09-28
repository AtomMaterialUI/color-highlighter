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

package com.mallowigi.config.home

import com.intellij.ide.ui.LafManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.SettingsCategory
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Property
import com.mallowigi.config.SettingsConfig
import com.mallowigi.listeners.ColorHighlighterNotifier

@State(
  name = "Color Highlighter Settings",
  storages = [Storage("color-highlighter.xml")],
  category = SettingsCategory.UI
)
class ColorHighlighterConfig : PersistentStateComponent<ColorHighlighterConfig>,
  SettingsConfig<ColorHighlighterSettingsForm, ColorHighlighterConfig> {
  @Property
  var isEnabled: Boolean = true

  @Property
  var isHexDetectEnabled: Boolean = true

  @Property
  var isJavaColorCtorEnabled: Boolean = true

  @Property
  var isJavaColorMethodEnabled: Boolean = true

  @Property
  var isKotlinColorCtorEnabled: Boolean = true

  @Property
  var isKotlinColorMethodEnabled: Boolean = true

  @Property
  var isRiderColorMethodEnabled: Boolean = true

  @Property
  var isTextEnabled: Boolean = true

  @Property
  var isMarkdownEnabled: Boolean = true

  @Property
  var version: String = "7.0.0"


  override fun getState(): ColorHighlighterConfig = this

  override fun loadState(state: ColorHighlighterConfig): Unit = XmlSerializerUtil.copyBean(state, this)

  override fun applySettings(form: ColorHighlighterSettingsForm) {
    isEnabled = form.getIsEnabled()
    isHexDetectEnabled = form.isHexDetectEnabled
    isJavaColorCtorEnabled = form.isJavaColorCtorEnabled
    isJavaColorMethodEnabled = form.isJavaColorMethodEnabled
    isKotlinColorCtorEnabled = form.isKotlinColorCtorEnabled
    isKotlinColorMethodEnabled = form.isKotlinColorMethodEnabled
    isRiderColorMethodEnabled = form.isRiderColorMethodEnabled
    isTextEnabled = form.isTextEnabled
    isMarkdownEnabled = form.isMarkdownEnabled

    ApplicationManager.getApplication().invokeAndWait { LafManager.getInstance().updateUI() }
    fireChanged()
  }

  override fun resetSettings() {
    isEnabled = true
    isHexDetectEnabled = true
    isJavaColorCtorEnabled = true
    isJavaColorMethodEnabled = true
    isKotlinColorCtorEnabled = true
    isKotlinColorMethodEnabled = true
    isRiderColorMethodEnabled = true
    isTextEnabled = true
    isMarkdownEnabled = true

    ApplicationManager.getApplication().invokeAndWait { LafManager.getInstance().updateUI() }
    fireChanged()
  }

  override fun fireChanged(): Unit = ApplicationManager.getApplication().messageBus
    .syncPublisher(ColorHighlighterNotifier.TOPIC)
    .configChanged(this)

  //region Enabled
  fun isEnabledChanged(isEnabled: Boolean): Boolean = this.isEnabled != isEnabled

  fun toggleEnabled() {
    isEnabled = !isEnabled
  }
  //endregion

  //region Hex Detect Enabled
  fun isHexDetectEnabledChanged(isEnabled: Boolean): Boolean = this.isHexDetectEnabled != isEnabled

  fun toggleHexDetectEnabled() {
    isHexDetectEnabled = !isHexDetectEnabled
  }
  //endregion

  //region Java Color Ctor Enabled
  fun isJavaColorCtorEnabledChanged(isEnabled: Boolean): Boolean = this.isJavaColorCtorEnabled != isEnabled

  fun toggleJavaColorCtorEnabled() {
    isJavaColorCtorEnabled = !isJavaColorCtorEnabled
  }
  //endregion

  //region Kotlin Color Ctor Enabled
  fun isKotlinColorCtorEnabledChanged(isEnabled: Boolean): Boolean = this.isKotlinColorCtorEnabled != isEnabled

  fun toggleKotlinColorCtorEnabled() {
    isKotlinColorCtorEnabled = !isKotlinColorCtorEnabled
  }
  //endregion

  //region Java Color Method Enabled
  fun isJavaColorMethodEnabledChanged(isEnabled: Boolean): Boolean = this.isJavaColorMethodEnabled != isEnabled

  fun toggleJavaColorMethodEnabled() {
    isJavaColorMethodEnabled = !isJavaColorMethodEnabled
  }
  //endregion

  //region Kotlin Color Method Enabled
  fun isKotlinColorMethodEnabledChanged(isEnabled: Boolean): Boolean = this.isKotlinColorMethodEnabled != isEnabled

  fun toggleKotlinColorMethodEnabled() {
    isKotlinColorMethodEnabled = !isKotlinColorMethodEnabled
  }
  //endregion

  //region Rider Color Method Enabled
  fun isRiderColorMethodEnabledChanged(isEnabled: Boolean): Boolean = this.isRiderColorMethodEnabled != isEnabled

  fun toggleRiderColorMethodEnabled() {
    isRiderColorMethodEnabled = !isRiderColorMethodEnabled
  }
  //endregion

  //region Text Enabled
  fun isTextEnabledChanged(isEnabled: Boolean): Boolean = this.isTextEnabled != isEnabled

  fun toggleTextEnabled() {
    isTextEnabled = !isTextEnabled
  }
  //endregion

  //region Markdown Enabled
  fun isMarkdownEnabledChanged(isEnabled: Boolean): Boolean = this.isMarkdownEnabled != isEnabled

  fun toggleMarkdownEnabled() {
    isMarkdownEnabled = !isMarkdownEnabled
  }
  //endregion


  companion object {
    @JvmStatic
    val instance: ColorHighlighterConfig
      get() = ApplicationManager.getApplication().getService(ColorHighlighterConfig::class.java)
  }
}
