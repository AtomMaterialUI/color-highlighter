package com.mallowigi.highlighters

import java.awt.Graphics2D

class BorderStylePainter : StylePainter {
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
    g2.color = color
    g2.drawRoundRect(x, y, width, height, arc, arc)
  }
}
