package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PhpVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof PhpFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof StringLiteralExpression)) {
      return;
    }

    final String value = ((StringLiteralExpression) element).getContents();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new PhpVisitor();
  }
}
