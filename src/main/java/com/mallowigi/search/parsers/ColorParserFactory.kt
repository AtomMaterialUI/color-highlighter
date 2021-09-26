/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Elior Boukhobza
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

package com.mallowigi.search.parsers;

import java.util.regex.Pattern;

public enum ColorParserFactory {
  ;
  private static final String COLOR_METHOD = "Color.";
  private static final String COLOR_UIRESOURCE_METHOD = "ColorUIResource.";
  private static final String RGB = "rgb";
  private static final String HSL = "hsl";
  private static final String OX = "0x";
  private static final String COLOR = "new Color";
  private static final String COLOR_UI_RESOURCE = "new ColorUIResource";
  private static final Pattern NO_HEX_PATTERN = Pattern.compile("(\\b[a-fA-F0-9]{3,8}\\b)");
  public static final String HASH = "#";

  @SuppressWarnings("IfStatementWithTooManyBranches")
  public static ColorParser getParser(final String text) {
    if (text.startsWith(HASH) && text.length() > 1) {
      return new HexColorParser(HASH);
    } else if (text.startsWith(RGB)) {
      return new RGBColorParser();
    } else if (text.startsWith(HSL)) {
      return new HSLColorParser();
    } else if (text.startsWith(OX)) {
      return new HexColorParser(OX);
//    } else if (text.startsWith(COLOR) || text.startsWith(COLOR_UI_RESOURCE)) {
//      return new ColorCtorParser();ß
    } else if (NO_HEX_PATTERN.matcher(text).find()) {
      return new HexColorParser("");
      //      } else if (text.startsWith(COLOR_METHOD)) {
      //        return new ColorMethodParser(COLOR_METHOD);
      //      } else if (text.startsWith(COLOR_UIRESOURCE_METHOD)) {
      //        return new ColorMethodParser(COLOR_UIRESOURCE_METHOD);
    } else {
      return new SVGColorParser();
    }
  }
}
