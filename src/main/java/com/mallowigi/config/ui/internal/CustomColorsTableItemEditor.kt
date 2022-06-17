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
package com.mallowigi.config.ui.internal

import com.intellij.util.Function
import com.intellij.util.ui.table.TableModelEditor.DialogItemEditor
import com.mallowigi.colors.SingleColor

/**
 * Associations table item editor
 *
 */
class CustomColorsTableItemEditor : DialogItemEditor<SingleColor> {
  override fun getItemClass(): Class<out SingleColor> = SingleColor::class.java

  override fun clone(item: SingleColor, forInPlaceEditing: Boolean): SingleColor = SingleColor(
    item.name,
    item.code
  )

  override fun applyEdited(oldItem: SingleColor, newItem: SingleColor): Unit = oldItem.apply(newItem)

  override fun isEditable(item: SingleColor): Boolean = !item.isEmpty

  override fun isEmpty(item: SingleColor): Boolean = item.isEmpty

  override fun edit(
    item: SingleColor,
    mutator: Function<in SingleColor, out SingleColor>,
    isAdd: Boolean,
  ) {
    val settings = clone(item, true)
    mutator.`fun`(item).apply(settings)
  }

}
