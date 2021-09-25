package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.json.psi.JsonFile;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JsonVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof JsonFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof JsonStringLiteral)) {
      return;
    }

    final JsonStringLiteral literal = (JsonStringLiteral) element;
    final String value = (String) literal.getValue();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new JsonVisitor();
  }
}
