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

import com.intellij.openapi.options.SearchableConfigurable
import com.mallowigi.ColorHighlighterBundle
import com.mallowigi.config.ConfigurableBase
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import java.util.*

class CustomColorsConfigurable : ConfigurableBase<CustomColorsForm, CustomColorsConfig>(), SearchableConfigurable {
  override val config: CustomColorsConfig
    get() = CustomColorsConfig.instance

  @Nls
  override fun getDisplayName(): String = ColorHighlighterBundle.message("CustomColorsForm.title")

  override fun getId(): String = ID

  override fun createForm(): CustomColorsForm = CustomColorsForm()

  override fun setFormState(form: CustomColorsForm?, config: CustomColorsConfig) {
    form?.setFormState(config)
  }

  override fun doApply(form: CustomColorsForm?, config: CustomColorsConfig): Unit = config.applySettings(form!!)

  override fun checkModified(form: CustomColorsForm?, config: CustomColorsConfig): Boolean =
    checkFormModified(form!!, config)

  private fun checkFormModified(form: CustomColorsForm, config: CustomColorsConfig): Boolean =
    Objects.requireNonNull(form)!!.isModified(config)

  companion object {
    @NonNls
    private const val ID = "CustomColorsConfig"
  }
}
