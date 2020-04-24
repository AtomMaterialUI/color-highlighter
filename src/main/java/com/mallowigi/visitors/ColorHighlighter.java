package com.mallowigi.visitors;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.Gray;
import com.mallowigi.GutterColorRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

final class ColorHighlighter {
  private static final HighlightInfoType COLOR_ELEMENT = new HighlightInfoType.HighlightInfoTypeImpl(
    HighlightSeverity.INFORMATION,
    DefaultLanguageHighlighterColors.CONSTANT
  );

  private static TextAttributes getAttributesFlyweight(@NotNull final Color color) {
    final TextAttributes attributes = new TextAttributes();
    final Color background = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    final Color mix = ColorUtil.mix(background, color, color.getAlpha() / 255.0D);

    return TextAttributes.fromFlyweight(
      attributes.getFlyweight()
                .withBackground(mix)
                .withForeground(ColorUtil.isDark(mix) ? Gray._254 : Gray._1));
  }

  static HighlightInfo highlightColor(final PsiElement element, final Color color) {
    return getHighlightInfoBuilder(color, element).range(element).create();
  }

  @NotNull
  private static HighlightInfo.Builder getHighlightInfoBuilder(final Color color, final PsiElement element) {
    return HighlightInfo.newHighlightInfo(COLOR_ELEMENT)
                        .gutterIconRenderer(new GutterColorRenderer(color, element))
                        .textAttributes(getAttributesFlyweight(color));
  }
}
