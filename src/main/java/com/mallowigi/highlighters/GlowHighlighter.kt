package com.mallowigi.highlighters

import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

class GlowHighlighter : Highlighter {
  override fun getAttributesFlyweight(color: Color): TextAttributes {
    val attributes = TextAttributes()
    return TextAttributes.fromFlyweight(attributes.flyweight)
  }
}
