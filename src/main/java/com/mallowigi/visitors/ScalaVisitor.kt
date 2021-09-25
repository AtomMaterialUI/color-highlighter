package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.scala.lang.psi.api.ScalaFile;
import org.jetbrains.plugins.scala.lang.psi.api.base.literals.ScIntegerLiteral;

import java.awt.*;

public class ScalaVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof ScalaFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof ScIntegerLiteral)) {
      return;
    }

    final ScIntegerLiteral literal = (ScIntegerLiteral) element;
    final String value = literal.getText();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new ScalaVisitor();
  }
}
