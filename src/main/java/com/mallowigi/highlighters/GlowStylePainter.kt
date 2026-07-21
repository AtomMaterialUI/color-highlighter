package com.mallowigi.highlighters

import java.awt.Color
import java.awt.Graphics2D

class GlowStylePainter : StylePainter {
  override fun paint(
    g2: Graphics2D,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    arc: Int,
    color: Color,
    mixedColor: Color
  ) {
    // Draw 3 concentric filled rounded rectangles with decreasing alpha for glow effect
    val red = (color.red * 0.8).toInt()
    val green = (color.green * 0.8).toInt()
    val blue = (color.blue * 0.8).toInt()

    // Outer glow (20% alpha) - filled
    val glowOuter = Color(red, green, blue, (color.alpha * 0.2).toInt())
    g2.color = glowOuter
    val outerArc = arc + 4
    g2.drawRoundRect(x - 3, y - 3, width + 6, height + 6, outerArc, outerArc)

    // Middle glow (40% alpha) - filled
    val glowMiddle = Color(red, green, blue, (color.alpha * 0.4).toInt())
    g2.color = glowMiddle
    val middleArc = arc + 2
    g2.drawRoundRect(x - 2, y - 2, width + 4, height + 4, middleArc, middleArc)

    // Inner outline (60% opacity)
    val glowInner = Color(red, green, blue, (color.alpha * 0.6).toInt())
    g2.color = glowInner
    g2.drawRoundRect(x - 1, y - 1, width + 2, height + 2, arc, arc)
  }
}
