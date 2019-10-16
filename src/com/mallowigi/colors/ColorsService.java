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
import com.intellij.openapi.util.Pair;
import com.thoughtworks.xstream.XStream;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class ColorsService {
  private static final String COLORS_XML = "/config/colors.xml";
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

  private static Colors parseColorsFromXML() {
    final URL xml = ColorsService.class.getResource(COLORS_XML);
    @NonNls final XStream xStream = new XStream();
    XStream.setupDefaultSecurity(xStream);
    xStream.allowTypesByWildcard(new String[]{"com.mallowigi.colors.*"});

    xStream.alias("colors", Colors.class);
    xStream.alias("color", SingleColor.class);

    xStream.useAttributeFor(SingleColor.class, "name");
    xStream.useAttributeFor(SingleColor.class, "code");

    try {
      return (Colors) xStream.fromXML(xml);
    } catch (final RuntimeException e) {
      return new Colors();
    }
  }

  private static int toColor(final int rgb) {
    return rgb & 0xffffff;
  }

  @NotNull
  public Map<Integer, String> getSVGColors() {
    return Collections.unmodifiableMap(svgColors);
  }

  @NotNull
  public Map<String, Integer> getSVGNames() {
    return Collections.unmodifiableMap(svgNames);
  }

  @NotNull
  public Map<Integer, String> getJavaColors() {
    return Collections.unmodifiableMap(javaColors);
  }

  @NotNull
  public Map<String, Integer> getJavaNames() {
    return Collections.unmodifiableMap(javaNames);
  }

  public String findSVGName(final Color color) {
    final int rgb = toColor(color.getRGB());

    return svgColors.get(rgb);
  }

  @Nullable
  public Color findSVGColor(final String name) {
    final Integer code = svgNames.get(name);
    if (code != null) {
      return new Color(code);
    }

    return null;
  }

  public String findJavaName(final Color color) {
    final int rgb = toColor(color.getRGB());

    return javaColors.get(rgb);
  }

  @Nullable
  public Color findJavaColor(final String name) {
    final Integer code = javaNames.get(name);
    if (code != null) {
      return new Color(code);
    }

    return null;
  }

  @SuppressWarnings("HardCodedStringLiteral")
  private void loadColors() {
    final Colors colors = parseColorsFromXML();

    svgColors = new TreeMap<>();
    svgNames = new TreeMap<>();

    for (final SingleColor col : colors.getColors()) {
      svgColors.put(col.getColorInt(), col.getName());
      svgNames.put(col.getName(), col.getColorInt());
    }

    @NonNls final Pair[] jcolors = {
        new Pair<>(Color.black, "black"),
        new Pair<>(Color.blue, "blue"),
        new Pair<>(Color.cyan, "cyan"),
        new Pair<>(Color.darkGray, "darkgray"),
        new Pair<>(Color.gray, "gray"),
        new Pair<>(Color.green, "green"),
        new Pair<>(Color.lightGray, "lightgray"),
        new Pair<>(Color.magenta, "magenta"),
        new Pair<>(Color.orange, "orange"),
        new Pair<>(Color.pink, "pink"),
        new Pair<>(Color.red, "red"),
        new Pair<>(Color.white, "white"),
        new Pair<>(Color.yellow, "yellow"),
        new Pair<>(Color.BLACK, "BLACK"),
        new Pair<>(Color.BLUE, "BLUE"),
        new Pair<>(Color.CYAN, "CYAN"),
        new Pair<>(Color.DARK_GRAY, "DARK_GRAY"),
        new Pair<>(Color.GRAY, "GRAY"),
        new Pair<>(Color.GREEN, "GREEN"),
        new Pair<>(Color.LIGHT_GRAY, "LIGHT_GRAY"),
        new Pair<>(Color.MAGENTA, "MAGENTA"),
        new Pair<>(Color.ORANGE, "ORANGE"),
        new Pair<>(Color.PINK, "PINK"),
        new Pair<>(Color.RED, "RED"),
        new Pair<>(Color.WHITE, "WHITE"),
        new Pair<>(Color.YELLOW, "YELLOW"),
    };

    javaColors = new TreeMap<>();
    javaNames = new TreeMap<>();

    for (final Pair<Color, String> jcolor : jcolors) {
      javaColors.put(toColor(jcolor.first.getRGB()), jcolor.second);
      javaNames.put(jcolor.second, toColor(jcolor.first.getRGB()));
    }

    //    for (int i = 0; i < colors.length; i += 2) {
    //      final int col = ((Color) colors[i]).getRGB() & 0xffffff;
    //      javaColors.put(col, (String) colors[i + 1]);
    //      javaNames.put((String) colors[i + 1], col);
    //    }
  }
}
