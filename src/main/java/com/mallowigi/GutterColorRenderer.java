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
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ColorLineMarkerProvider;
import com.intellij.ui.ColorPicker;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

final class GutterColorRenderer extends GutterIconRenderer {
  private static final int ICON_SIZE = 12;
  @NonNls
  final Color color;
  final PsiElement elementToAnnotate;

  GutterColorRenderer(final Color color, final PsiElement elementToAnnotate) {
    this.color = color;
    this.elementToAnnotate = elementToAnnotate;
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

  @NotNull
  @Override
  public String getTooltipText() {
    return "Choose Color";
  }

  @SuppressWarnings("OverlyComplexAnonymousInnerClass")
  @NotNull
  @NonNls
  @Override
  public AnAction getClickAction() {
    return new AnAction("Choose Color...") {
      @Override
      public void actionPerformed(@NotNull final AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
          return;
        }

        final Color currentColor = color;
        if (currentColor == null) {
          return;
        }

        ColorPicker.showColorPickerPopup(e.getProject(), currentColor, (newColor, l) -> applyColor(currentColor, newColor, editor));
      }

      private void applyColor(final Color currentColor, final Color newColor, final Editor editor) {
        if (newColor == null || newColor.equals(currentColor)) {
          return;
        }

        @NonNls final String newColorHex = String.format("\"#%s\"", ColorUtil.toHex(newColor));
        final Project project = elementToAnnotate.getProject();

        WriteCommandAction.writeCommandAction(project, elementToAnnotate.getContainingFile()).run(
          () -> editor.getDocument().replaceString(
            elementToAnnotate.getTextOffset(),
            elementToAnnotate.getTextOffset() + newColorHex.length(),
            newColorHex)
        );
      }
    };
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
    return color.equals(renderer.color);
  }

  @SuppressWarnings("ObjectInstantiationInEqualsHashCode")
  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

}
