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

import com.intellij.openapi.util.NlsContexts
import com.mallowigi.ColorHighlighterBundle.message
import com.mallowigi.ColorHighlighterIcons.Highlighting.BACKGROUND_ICON
import com.mallowigi.ColorHighlighterIcons.Highlighting.BORDER_ICON
import com.mallowigi.ColorHighlighterIcons.Highlighting.FOREGROUND_ICON
import com.mallowigi.ColorHighlighterIcons.Highlighting.INLINE_ICON
import com.mallowigi.ColorHighlighterIcons.Highlighting.UNDERLINE_ICON
import javax.swing.Icon

enum class HighlightingStyles(
  @NlsContexts.Label val type: String,
  val icon: Icon
) {
  BACKGROUND(message("HighlightingStyles.background"), BACKGROUND_ICON),
  BORDER(message("HighlightingStyles.border"), BORDER_ICON),
  UNDERLINE(message("HighlightingStyles.underline"), UNDERLINE_ICON),
  FOREGROUND(message("HighlightingStyles.foreground"), FOREGROUND_ICON),
  INLINE(message("HighlightingStyles.inline"), INLINE_ICON);
}
