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

@file:Suppress("UnstableApiUsage")

package com.mallowigi.utils

import com.intellij.ui.svg.SvgAttributePatcher
import com.intellij.util.SVGLoader
import java.awt.Color

class IconColorPatcher(val color: Color) : SVGLoader.SvgElementColorPatcherProvider {
  override fun attributeForPath(path: String?): SvgAttributePatcher = object : SvgAttributePatcher {
    override fun patchColors(attributes: MutableMap<String, String>) {
      val color = when (attributes[PATCHFILL]) {
        "accent" -> "#${color.toHex()}"
        else -> null
      }

      if (color != null) {
        attributes[FILL] = color
      }
    }

    override fun digest(): LongArray {
      val accentColor = color.toHex()

      return longArrayOf(
        accentColor.toHash(),
      )
    }
  }

  companion object {
    private const val PATCHFILL = "patchFill"
    private const val FILL = "fill"
  }
}