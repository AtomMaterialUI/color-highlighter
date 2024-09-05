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

@file:Suppress("PropertyName")

package com.mallowigi

import com.intellij.ui.IconManager
import javax.swing.Icon

fun load(path: String): Icon = IconManager.getInstance().getIcon(path, ColorHighlighterIcons::class.java.classLoader)

object ColorHighlighterIcons {
  object Gutter {
    val Inline: Icon = load("icons/gutter/inline.svg")
  }

  object Settings {
    val MAIN_ICON: Icon = load("icons/settings/icon.svg")
    val HEX_ICON: Icon = load("icons/settings/hex.svg")
    val COLOR_NAMES_ICON: Icon = load("icons/settings/colorNames.svg")
    val TUPLE_ICON: Icon = load("icons/settings/tuple.svg")
    val ANDROID_ICON: Icon = load("icons/settings/android.svg")
    val CSS_ICON: Icon = load("icons/settings/css.svg")
    val MARKDOWN_ICON: Icon = load("icons/settings/markdown.svg")
    val MARKUP_ICON: Icon = load("icons/settings/markup.svg")
    val JAVA_ICON: Icon = load("icons/settings/java.svg")
    val KOTLIN_ICON: Icon = load("icons/settings/kotlin.svg")
    val SCALA_ICON: Icon = load("icons/settings/scala.svg")
    val XCODE_ICON: Icon = load("icons/settings/xcode.svg")
    val TEXT_ICON: Icon = load("icons/settings/text.svg")
  }

  object Highlighting {
    val BACKGROUND_ICON: Icon = load("icons/highlighting/background.svg")
    val BORDER_ICON: Icon = load("icons/highlighting/border.svg")
    val FOREGROUND_ICON: Icon = load("icons/highlighting/foreground.svg")
    val UNDERLINE_ICON: Icon = load("icons/highlighting/underline.svg")
    val INLINE_ICON: Icon = load("icons/highlighting/inline.svg")
    val DISABLED_ICON: Icon = load("icons/highlighting/disabled.svg")
  }

  object Other {
    val INLINE = load("icons/other/inline.svg")
  }

}
