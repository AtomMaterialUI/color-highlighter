package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings("CallToSuspiciousStringMethod")
public class AppCodeVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file.toString().contains("OCFile") || file.getClass().toString().contains("SwiftFile"); // NON-NLS
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    final String type = PsiUtilCore.getElementType(element).toString();
    if (!"STRING_LITERAL".equals(type) && !"ISTRING_CONTENT".equals(type)) { // NON-NLS
      return;
    }

    final String value = element.getText();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new AppCodeVisitor();
  }
}
