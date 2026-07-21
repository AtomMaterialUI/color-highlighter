package com.mallowigi.highlighters

import java.awt.Graphics2D

/**
 * Interface for painting different rounded highlight styles.
 */
interface StylePainter {
  fun paint(g2: Graphics2D, x: Int, y: Int, width: Int, height: Int, arc: Int, color: java.awt.Color, mixedColor: java.awt.Color)
}
