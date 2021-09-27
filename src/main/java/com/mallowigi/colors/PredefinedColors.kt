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
import com.thoughtworks.xstream.XStream
import java.awt.Color
import java.util.*

/**
 * PredefinedColors: This is the service containing the predefined colors from the XML file
 *
 */
class PredefinedColors {

  companion object {
    private const val COLORS_XML = "/config/colors.xml"

    @JvmStatic
    val instance: PredefinedColors
      get() = ApplicationManager.getApplication().getService(PredefinedColors::class.java)
  }

  private var svgColors: MutableMap<Int, String?>? = null
  private var svgNames: MutableMap<String?, Int>? = null

  private var javaColors: MutableMap<Int, String>? = null
  private var javaNames: MutableMap<String, Int>? = null

  /**
   * Find svgName from Color
   *
   * @param color
   * @return
   */
  fun findSVGName(color: Color): String? {
    val rgb = toColor(color.rgb)
    return (svgColors ?: return null)[rgb]
  }

  /**
   * Find a svgColor from a name
   *
   * @param name
   * @return
   */
  fun findSVGColor(name: String?): Color? {
    val code = (svgNames ?: return null)[name]
    return if (code != null) Color(code) else null
  }

  /**
   * Find javaName from color
   *
   * @param color
   * @return
   */
  fun findJavaName(color: Color): String? {
    val rgb = toColor(color.rgb)
    return (javaColors ?: return null)[rgb]
  }

  /**
   * Find java color from name
   *
   * @param name
   * @return
   */
  fun findJavaColor(name: String): Color? {
    val code = (javaNames ?: return null)[name]
    return if (code != null) Color(code) else null
  }

  /**
   * Load colors from XML
   *
   */
  private fun loadColors() {
    parseColorsFromXML().also {
      loadSVGColors(it)
      loadJavaColors(it)
    }
  }

  private fun loadSVGColors(colors: Colors) {
    svgColors = TreeMap()
    svgNames = TreeMap()

    for (col: SingleColor in colors.svgColors!!) {
      (svgColors as TreeMap<Int, String?>)[col.colorInt] = col.name
      (svgNames as TreeMap<String?, Int>)[col.name] = col.colorInt
    }
  }


  private fun loadJavaColors(colors: Colors) {
    javaColors = TreeMap()
    javaNames = TreeMap()

    for (col: SingleColor in colors.javaColors!!) {
      (javaColors as TreeMap<Int, String>)[col.colorInt] = col.name
      (javaNames as TreeMap<String, Int>)[col.name] = col.colorInt
    }
  }


  private fun parseColorsFromXML(): Colors {
    val xml = PredefinedColors::class.java.getResource(COLORS_XML)
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

  init {
    loadColors()
  }
}
