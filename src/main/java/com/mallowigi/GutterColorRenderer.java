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

package com.mallowigi;

import com.intellij.codeInsight.daemon.LineMarkerSettings;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.ui.ColorLineMarkerProvider;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

final class GutterColorRenderer extends GutterIconRenderer {
  private static final int ICON_SIZE = 12;
  @NonNls
  private final Color color;

  GutterColorRenderer(final Color color) {
    this.color = color;
  }

  static boolean isGutterColorEnabled() {
    return LineMarkerSettings.getSettings().isEnabled(ColorLineMarkerProvider.INSTANCE);
  }

  @SuppressWarnings("UnstableApiUsage")
  @NotNull
  @Override
  public Icon getIcon() {
    if (color != null) {
      return JBUI.scale(new ColorIcon(ICON_SIZE, color));
    }
    return JBUI.scale(EmptyIcon.create(ICON_SIZE));

  }

  @Nullable
  @Override
  public String getTooltipText() {
    return "Choose Color";
  }

  @Override
  public boolean isNavigateAction() {
    return true;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final GutterColorRenderer renderer = (GutterColorRenderer) obj;
    return color.equals(renderer.getColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

  public Color getColor() {
    return color;
  }
}
