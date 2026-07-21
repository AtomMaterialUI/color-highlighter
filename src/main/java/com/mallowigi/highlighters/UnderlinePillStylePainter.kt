package com.mallowigi.highlighters

import java.awt.Graphics2D
import kotlin.math.max
import kotlin.math.min

class UnderlinePillStylePainter : StylePainter {
  override fun paint(
    g2: Graphics2D,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    arc: Int,
    color: java.awt.Color,
    mixedColor: java.awt.Color
  ) {
    val underlineHeight = max(2, height / 5)
    val underlineY = y + height - underlineHeight
    val underlineArc = min(underlineHeight, arc)

    g2.color = mixedColor
    g2.fillRoundRect(x, underlineY, width, underlineHeight, underlineArc, underlineArc)
  }
}
