/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
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

package com.mallowigi.colors;

import com.intellij.openapi.components.ServiceManager;
import com.mallowigi.utils.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public final class ColorsService {
  private Map<Integer, String> svgColors;
  private Map<String, Integer> svgNames;
  private Map<Integer, String> javaColors;
  private Map<String, Integer> javaNames;

  private ColorsService() {
    loadColors();
  }

  public static ColorsService getInstance() {
    return ServiceManager.getService(ColorsService.class);
  }

  @NotNull
  public Map<Integer, String> getSVGColors() {
    return svgColors;
  }

  @NotNull
  public Map<String, Integer> getSVGNames() {
    return svgNames;
  }

  @NotNull
  public Map<Integer, String> getJavaColors() {
    return javaColors;
  }

  @NotNull
  public Map<String, Integer> getJavaNames() {
    return javaNames;
  }

  public String findSVGName(final Color color) {
    final int rgb = color.getRGB() & 0xffffff;

    return svgColors.get(rgb);
  }

  public Color findSVGColor(final String name) {
    final Integer code = svgNames.get(name);
    if (code != null) {
      return new Color(code);
    }

    return null;
  }

  public String findJavaName(final Color color) {
    final int rgb = color.getRGB() & 0xffffff;

    return javaColors.get(rgb);
  }

  public Color findJavaColor(final String name) {
    final Integer code = javaNames.get(name);
    if (code != null) {
      return new Color(code);
    }

    return null;
  }

  private void loadColors() {
    try {
      svgColors = new TreeMap<>();
      svgNames = new TreeMap<>();

      final Document doc = XmlUtil.loadDocument("/config/colors.xml");
      final Element root = doc.getDocumentElement();
      final NodeList ents = root.getElementsByTagName("color");
      for (int i = 0; i < ents.getLength(); i++) {
        final Element ent = (Element) ents.item(i);

        svgColors.put(Integer.valueOf(ent.getAttribute("code"), 16), ent.getAttribute("name"));
        svgNames.put(ent.getAttribute("name"), Integer.valueOf(ent.getAttribute("code"), 16));
      }
    } catch (final Exception e) {
    }

    final Object[] colors = {
        Color.black,
        "black",
        Color.blue,
        "blue",
        Color.cyan,
        "cyan",
        Color.darkGray,
        "darkgray",
        Color.gray,
        "gray",
        Color.green,
        "green",
        Color.lightGray,
        "lightgray",
        Color.magenta,
        "magenta",
        Color.orange,
        "orange",
        Color.pink,
        "pink",
        Color.red,
        "red",
        Color.white,
        "white",
        Color.yellow,
        "yellow",
        Color.BLACK,
        "BLACK",
        Color.BLUE,
        "BLUE",
        Color.CYAN,
        "CYAN",
        Color.DARK_GRAY,
        "DARK_GRAY",
        Color.GRAY,
        "GRAY",
        Color.GREEN,
        "GREEN",
        Color.LIGHT_GRAY,
        "LIGHT_GRAY",
        Color.MAGENTA,
        "MAGENTA",
        Color.ORANGE,
        "ORANGE",
        Color.PINK,
        "PINK",
        Color.RED,
        "RED",
        Color.WHITE,
        "WHITE",
        Color.YELLOW,
        "YELLOW"
    };

    javaColors = new TreeMap<>();
    javaNames = new TreeMap<>();
    for (int i = 0; i < colors.length; i += 2) {
      final int col = ((Color) colors[i]).getRGB() & 0xffffff;
      javaColors.put(col, (String) colors[i + 1]);
      javaNames.put((String) colors[i + 1], col);
    }
  }
}
