package com.mallowigi.highlighters

object StylePainterFactory {
  fun getPainter(style: RoundedPaintStyle): StylePainter = when (style) {
    RoundedPaintStyle.BACKGROUND -> BackgroundStylePainter()
    RoundedPaintStyle.BORDER -> BorderStylePainter()
    RoundedPaintStyle.UNDERLINE_PILL -> UnderlinePillStylePainter()
    RoundedPaintStyle.GLOW -> GlowStylePainter()
  }
}
