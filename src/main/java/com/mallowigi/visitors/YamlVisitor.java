package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ArrayUtil;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLFile;

import java.awt.*;

public class YamlVisitor extends ColorVisitor {
  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return file instanceof YAMLFile;
  }

  @Override
  public void visit(@NotNull final PsiElement element) {
    if (!ArrayUtil.contains(PsiUtilCore.getElementType(element).toString(), "text", "scalar string", "comment", "scalar dstring")) {
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
    return new YamlVisitor();
  }
}
