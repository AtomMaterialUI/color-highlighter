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

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.mallowigi.ColorHighlightSettings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings("CallToSuspiciousStringMethod")
public class JavaColorUiResourceAnnotator extends ColorAnnotator {
  @Override
  boolean isTargetElement(final PsiElement element) {
    final Object text = element.getText();
    return text != null;
  }

  @Override
  public final void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
    if (!ColorHighlightSettings.isPluginEnabled()) {
      return;
    }

    if (!"INTEGER_LITERAL".equals(PsiUtilCore.getElementType(element).toString())) { // NON-NLS
      return;
    }

    if (!isTargetElement(element)) {
      return;
    }

    final String value = element.getText();
    final Color color = getColor(value);

    if (color != null) {
      doAnnotate(element, color, holder);
    }
  }

}
