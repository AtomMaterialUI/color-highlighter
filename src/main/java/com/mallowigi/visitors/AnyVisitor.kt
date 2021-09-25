package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLiteralValue;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AnyVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return true;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof PsiLiteralValue)) {
      return;
    }

    final PsiLiteralValue literal = (PsiLiteralValue) element;
    final Object value = literal.getValue();

    if (value instanceof String) {
      final Color color = ColorSearchEngine.getColor((String) value);

      if (color != null) {
        highlight(element, color);
      }
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new AnyVisitor();
  }
}
