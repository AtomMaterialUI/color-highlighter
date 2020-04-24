package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.mallowigi.ColorHighlightSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public abstract class ColorVisitor implements HighlightVisitor {
  @Nullable
  private HighlightInfoHolder myHolder;
  @Nullable
  private ColorHighlighter myColorHighlighter;

  @Override
  public boolean suitableForFile(@NotNull final PsiFile file) {
    return true;
  }

  @Override
  public abstract void visit(@NotNull final PsiElement element);

  @Override
  public boolean analyze(@NotNull final PsiFile file,
                         final boolean updateWholeFile,
                         @NotNull final HighlightInfoHolder holder,
                         @NotNull final Runnable action) {

    myHolder = holder;
    myColorHighlighter = new ColorHighlighter();

    try {
      action.run();
    } finally {
      myHolder = null;
      myColorHighlighter = null;
    }
    return true;
  }

  @NotNull
  @Override
  public abstract HighlightVisitor clone();

  void highlight(final PsiElement element, final Color color) {
    assert myHolder != null;
    if (!ColorHighlightSettings.isPluginEnabled()) {
      return;
    }

    myHolder.add(ColorHighlighter.highlightColor(element, color));
  }

  private ColorHighlighter getHighlighter() {
    return myColorHighlighter;
  }
}
