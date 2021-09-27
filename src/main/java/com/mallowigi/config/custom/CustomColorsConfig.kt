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

package com.mallowigi.config.custom

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.SettingsCategory
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Property
import com.mallowigi.colors.CustomColors
import com.mallowigi.colors.SingleColor
import com.mallowigi.config.SettingsConfig
import com.mallowigi.listeners.CustomColorsNotifier
import java.util.*

@State(
  name = "Custom Colors",
  storages = [Storage("color-highlighter.xml")],
  category = SettingsCategory.UI
)
class CustomColorsConfig : PersistentStateComponent<CustomColorsConfig>,
  SettingsConfig<CustomColorsForm, CustomColorsConfig> {
  @Property
  var customColors: CustomColors = CustomColors()

  override fun getState(): CustomColorsConfig = this

  override fun loadState(state: CustomColorsConfig): Unit = XmlSerializerUtil.copyBean(state, this)

  override fun applySettings(form: CustomColorsForm) {
    customColors = form.customColors

    fireChanged()
  }

  override fun resetSettings() {
    customColors = CustomColors()
  }

  override fun fireChanged(): Unit = ApplicationManager.getApplication().messageBus
    .syncPublisher(CustomColorsNotifier.TOPIC)
    .configChanged(this)

  //region Custom Colors
  fun isCustomColorsModified(fileAssociations: List<SingleColor>): Boolean =
    !Objects.deepEquals(this.customColors, fileAssociations)
  //endregion

  companion object {
    @JvmStatic
    val instance: CustomColorsConfig
      get() = ApplicationManager.getApplication().getService(CustomColorsConfig::class.java)
  }

}
