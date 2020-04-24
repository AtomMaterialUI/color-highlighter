package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyPlainStringElement;
import com.jetbrains.python.psi.PyStringElement;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PythonVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof PyFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof PyPlainStringElement)) {
      return;
    }

    final String value = ((PyStringElement) element).getContent();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new PythonVisitor();
  }
}
