package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.lang.properties.psi.impl.PropertyValueImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PropertiesVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof PropertiesFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!(element instanceof PropertyValueImpl)) {
      return;
    }

    final PropertyValueImpl literal = (PropertyValueImpl) element;
    final String value = literal.getText();
    final Color color = ColorSearchEngine.getColor(value);

    if (color != null) {
      highlight(element, color);
    }
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new PropertiesVisitor();
  }
}
