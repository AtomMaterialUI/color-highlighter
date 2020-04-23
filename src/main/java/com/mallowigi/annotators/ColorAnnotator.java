/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Elior Boukhobza
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

package com.mallowigi.annotators;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.Gray;
import com.mallowigi.ColorHighlightSettings;
import com.mallowigi.GutterColorRenderer;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorAnnotator implements Annotator {

  boolean isTargetElement(final PsiElement element) {
    final Object text = ((PsiLiteralValue) element).getValue();
    return text instanceof String;
  }

  static Color getColor(final String colorText) {
    return ColorSearchEngine.getColor(colorText);
  }

  static void doAnnotate(@NotNull final PsiElement elementToAnnotate,
                         @NotNull final Color color,
                         @NotNull final AnnotationHolder holder) {
    AnnotationBuilder annotationBuilder = holder.newAnnotation(HighlightSeverity.INFORMATION, "");
    annotationBuilder = annotationBuilder
      .range(elementToAnnotate)
      .textAttributes(createTextAttributeKey(color));

    if (GutterColorRenderer.isGutterColorEnabled()) {
      annotationBuilder = annotationBuilder.gutterIconRenderer(new GutterColorRenderer(color, elementToAnnotate));
    }
    annotationBuilder.create();
  }

  private static TextAttributesKey createTextAttributeKey(@NotNull final Color color) {
    final TextAttributes attributes = new TextAttributes();
    final Color background = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    final Color mix = ColorUtil.mix(background, color, color.getAlpha() / 255.0D);
    attributes.setBackgroundColor(mix);
    attributes.setForegroundColor(ColorUtil.isDark(mix) ? Gray._254 : Gray._1);

    return TextAttributesKey.createTextAttributesKey("MT_COLOR_" + mix.hashCode(), attributes);
  }

  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
    if (!ColorHighlightSettings.isPluginEnabled()) {
      return;
    }

    if (!(element instanceof PsiLiteralValue)) {
      return;
    }

    if (!isTargetElement(element)) {
      return;
    }

    final PsiLiteralValue literal = (PsiLiteralValue) element;
    final String value = (String) literal.getValue();
    final Color color = getColor(value);

    if (color != null) {
      doAnnotate(element, color, holder);
    }
  }

}
