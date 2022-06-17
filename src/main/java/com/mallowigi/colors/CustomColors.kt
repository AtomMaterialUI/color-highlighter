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
package com.mallowigi.colors

import com.intellij.util.xmlb.annotations.Property
import com.intellij.util.xmlb.annotations.XCollection
import java.awt.Color
import java.io.Serializable
import java.util.Map.copyOf

/**
 * This is the serialized data class for user custom colors from xml
 */
@Suppress("MemberNameEqualsClassName", "unused")
class CustomColors : Serializable {
  @Property
  @XCollection
  private val customColors: MutableMap<String, SingleColor>

  constructor() {
    customColors = mutableMapOf()
  }

  constructor(associations: Map<String, SingleColor>) {
    customColors = copyOf(associations)
  }

  constructor(associations: List<SingleColor>) {
    customColors = associations.associateBy { it.name }.toMutableMap()
  }

  /**
   * Has the association?
   *
   * @param name
   */
  private fun has(name: String): Boolean = customColors.containsKey(name)

  /**
   * Get an association
   *
   * @param name
   */
  fun get(name: String): SingleColor? = customColors[name]

  /**
   * Add association to map if not exists
   *
   * @param name
   * @param assoc
   */
  fun set(name: String, assoc: SingleColor) {
    if (has(name)) return

    customColors[name] = assoc
  }

  /**
   * get the association values
   *
   * @return
   */
  private fun values(): List<SingleColor> = customColors.values.toList()

  fun findCustomColor(colorName: String): SingleColor? =
    values().stream()
      .filter { (name, _): SingleColor -> name == colorName }
      .findAny()
      .orElse(null)

  fun getTheAssociations(): List<SingleColor> = values()

  operator fun contains(text: String): Boolean = customColors.containsKey(text)

  fun getColor(name: String): Color? {
    val foundColor = customColors[name] ?: return null
    return Color(foundColor.colorInt)
  }

}
