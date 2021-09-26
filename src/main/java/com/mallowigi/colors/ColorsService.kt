/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package com.mallowigi.colors

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Pair
import com.mallowigi.colors.ColorsService
import com.thoughtworks.xstream.XStream
import java.awt.Color
import java.util.*

class ColorsService private constructor() {
  private var svgColors: MutableMap<Int, String?>? = null
  private var svgNames: MutableMap<String?, Int>? = null
  private var javaColors: MutableMap<Int, String>? = null
  private var javaNames: MutableMap<String, Int>? = null

  val sVGColors: Map<Int, String?>
    get() = Collections.unmodifiableMap(svgColors)

  val sVGNames: Map<String?, Int>
    get() = Collections.unmodifiableMap(svgNames)

  fun getJavaColors(): Map<Int, String> = Collections.unmodifiableMap(javaColors)

  fun getJavaNames(): Map<String, Int> = Collections.unmodifiableMap(javaNames)

  fun findSVGName(color: Color): String? {
    val rgb = toColor(color.rgb)
    return (svgColors ?: return null)[rgb]
  }

  fun findSVGColor(name: String?): Color? {
    val code = (svgNames ?: return null)[name]
    return if (code != null) Color(code) else null
  }

  fun findJavaName(color: Color): String? {
    val rgb = toColor(color.rgb)
    return (javaColors ?: return null)[rgb]
  }

  fun findJavaColor(name: String): Color? {
    val code = (javaNames ?: return null)[name]
    return if (code != null) Color(code) else null
  }

  private fun loadColors() {
    val colors = parseColorsFromXML()
    svgColors = TreeMap()
    svgNames = TreeMap()
    for (col: SingleColor in colors.colors ?: return) {
      (svgColors as TreeMap<Int, String?>)[col.colorInt] = col.name
      (svgNames as TreeMap<String?, Int>)[col.name] = col.colorInt
    }
    val jcolors = arrayOf<Pair<Color, String>>(
      Pair(Color.black, "black"),
      Pair(Color.blue, "blue"),
      Pair(Color.cyan, "cyan"),
      Pair(Color.darkGray, "darkgray"),
      Pair(Color.gray, "gray"),
      Pair(Color.green, "green"),
      Pair(Color.lightGray, "lightgray"),
      Pair(Color.magenta, "magenta"),
      Pair(Color.orange, "orange"),
      Pair(Color.pink, "pink"),
      Pair(Color.red, "red"),
      Pair(Color.white, "white"),
      Pair(Color.yellow, "yellow"),
      Pair(Color.BLACK, "BLACK"),
      Pair(Color.BLUE, "BLUE"),
      Pair(Color.CYAN, "CYAN"),
      Pair(Color.DARK_GRAY, "DARK_GRAY"),
      Pair(Color.GRAY, "GRAY"),
      Pair(Color.GREEN, "GREEN"),
      Pair(Color.LIGHT_GRAY, "LIGHT_GRAY"),
      Pair(Color.MAGENTA, "MAGENTA"),
      Pair(Color.ORANGE, "ORANGE"),
      Pair(Color.PINK, "PINK"),
      Pair(Color.RED, "RED"),
      Pair(Color.WHITE, "WHITE"),
      Pair(Color.YELLOW, "YELLOW"))
    javaColors = TreeMap()
    javaNames = TreeMap()
    for (jcolor: Pair<Color, String> in jcolors) {
      (javaColors as TreeMap<Int, String>)[toColor(jcolor.first.rgb)] = jcolor.second
      (javaNames as TreeMap<String, Int>)[jcolor.second] = toColor(jcolor.first.rgb)
    }
  }

  companion object {
    private const val COLORS_XML = "/config/colors.xml"

    @JvmStatic
    val instance: ColorsService
      get() = ApplicationManager.getApplication().getService(ColorsService::class.java)

    private fun parseColorsFromXML(): Colors {
      val xml = ColorsService::class.java.getResource(COLORS_XML)
      val xStream = XStream()
      XStream.setupDefaultSecurity(xStream)
      xStream.allowTypesByWildcard(arrayOf("com.mallowigi.colors.*"))
      xStream.alias("colors", Colors::class.java)
      xStream.alias("color", SingleColor::class.java)
      xStream.useAttributeFor(SingleColor::class.java, "name")
      xStream.useAttributeFor(SingleColor::class.java, "code")
      return try {
        xStream.fromXML(xml) as Colors
      } catch (e: RuntimeException) {
        Colors()
      }
    }

    private fun toColor(rgb: Int): Int = rgb and 0xffffff
  }

  init {
    loadColors()
  }
}
