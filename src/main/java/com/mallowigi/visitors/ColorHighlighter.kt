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

package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.Gray;
import com.mallowigi.gutter.GutterColorRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

final class ColorHighlighter {
  private static final HighlightInfoType COLOR_ELEMENT = new HighlightInfoType.HighlightInfoTypeImpl(
    HighlightSeverity.INFORMATION,
    DefaultLanguageHighlighterColors.CONSTANT
  );

  private static TextAttributes getAttributesFlyweight(@NotNull final Color color) {
    final TextAttributes attributes = new TextAttributes();
    final Color background = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    final Color mix = ColorUtil.mix(background, color, color.getAlpha() / 255.0D);

    return TextAttributes.fromFlyweight(
      attributes.getFlyweight()
        .withBackground(mix)
        .withForeground(ColorUtil.isDark(mix) ? Gray._254 : Gray._1));
  }

  static HighlightInfo highlightColor(final PsiElement element, final Color color) {
    return getHighlightInfoBuilder(color).range(element).create();
  }

  @NotNull
  private static HighlightInfo.Builder getHighlightInfoBuilder(final Color color) {
    return HighlightInfo.newHighlightInfo(COLOR_ELEMENT)
      .gutterIconRenderer(new GutterColorRenderer(color))
      .textAttributes(getAttributesFlyweight(color));
  }
}
