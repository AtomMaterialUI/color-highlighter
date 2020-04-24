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
import com.intellij.ui.ColorChooser;
import com.intellij.ui.ColorLineMarkerProvider;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.EmptyIcon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class GutterColorRenderer extends GutterIconRenderer {
  private static final int ICON_SIZE = 12;
  @NonNls
  private final Color color;
  private final PsiElement elementToAnnotate;

  public GutterColorRenderer(final Color color, final PsiElement elementToAnnotate) {
    this.color = color;
    this.elementToAnnotate = elementToAnnotate;
  }

  public static boolean isGutterColorEnabled() {
    return LineMarkerSettings.getSettings().isEnabled(ColorLineMarkerProvider.INSTANCE);
  }

  @NotNull
  @Override
  public Icon getIcon() {
    if (color != null) {
      return JBUIScale.scaleIcon(new ColorIcon(ICON_SIZE, color));
    }
    return JBUIScale.scaleIcon(EmptyIcon.create(ICON_SIZE));

  }

  @NotNull
  @Override
  public String getTooltipText() {
    return ColorHighlighterBundle.message("choose.color");
  }

  @SuppressWarnings("OverlyComplexAnonymousInnerClass")
  @NotNull
  @NonNls
  @Override
  public AnAction getClickAction() {
    return new AnAction(ColorHighlighterBundle.message("choose.color1")) {
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

        final Color newColor = ColorChooser.chooseColor(
          editor.getProject(),
          editor.getComponent(),
          ColorHighlighterBundle.message("replace.color"),
          currentColor,
          true
        );
        applyColor(currentColor, newColor, editor, true);
        //        ColorPicker.showColorPickerPopup(e.getProject(), currentColor, (newColor, l) -> applyColor(currentColor, newColor,
        //       editor));
      }

      private void applyColor(final Color currentColor, final Color newColor, final Editor editor, final boolean withAlpha) {
        if (newColor == null || newColor.equals(currentColor)) {
          return;
        }

        @NonNls final String newColorHex = String.format("#%s", ColorUtil.toHex(newColor, withAlpha));
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

  @NotNull
  @Override
  public Alignment getAlignment() {
    return Alignment.RIGHT;
  }
}
