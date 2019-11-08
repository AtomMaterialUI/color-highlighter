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

import com.mallowigi.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.StringTokenizer;

public final class HSLColorParser implements ColorParser {
  /**
   * Parse a color in the hsl[a](h, s, l[, a]) format
   */
  @SuppressWarnings({"UseOfStringTokenizer",
    "ReuseOfLocalVariable",
    "DuplicatedCode",
    "IfMayBeConditional"})
  @Nullable
  private static Color parseHSL(@NotNull final String text) {
    float a = 1.0f;
    final int hue;
    final int saturation;
    final int luminance;
    final int parenStart = text.indexOf(ColorUtils.OPEN_PAREN);
    final int parenEnd = text.indexOf(ColorUtils.CLOSE_PAREN);

    if (parenStart == -1 || parenEnd == -1) {
      return null;
    }

    final StringTokenizer tokenizer = new StringTokenizer(text.substring(parenStart + 1, parenEnd), ColorUtils.COMMA);
    if (tokenizer.countTokens() < 3) {
      return null;
    }

    // Parse h, s, l and a
    String part = tokenizer.nextToken().trim();

    hue = Integer.parseInt(part);

    part = tokenizer.nextToken().trim();
    if (part.endsWith(ColorUtils.PERCENT)) {
      saturation = Integer.parseInt(part.substring(0, part.length() - 1));
    } else {
      saturation = Integer.parseInt(part);
    }

    part = tokenizer.nextToken().trim();
    if (part.endsWith(ColorUtils.PERCENT)) {
      luminance = Integer.parseInt(part.substring(0, part.length() - 1));
    } else {
      luminance = Integer.parseInt(part);
    }

    if (tokenizer.hasMoreTokens()) {
      part = tokenizer.nextToken().trim();
      a = Float.parseFloat(part);
    }

    return ColorUtils.getHSLa(hue, saturation, luminance, a);
  }

  @Override
  public Color parseColor(final String text) {
    return parseHSL(text);
  }
}
