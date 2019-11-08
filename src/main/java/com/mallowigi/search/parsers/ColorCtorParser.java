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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.StringTokenizer;

public final class ColorCtorParser implements ColorParser {
  @SuppressWarnings({"MagicCharacter"
  })
  @Nullable
  private static Color parseConstructor(@NotNull final String text) {
    boolean isFloat = false;
    float fr = 0.0f;
    float fg = 0.0f;
    float fb = 0.0f;
    float fa = 1.0f;
    int ir = 0;
    int ig = 0;
    int ib = 0;
    int ia = 255;
    boolean alpha = false;
    final int ps = text.indexOf('(');
    final int pe = text.indexOf(')');
    if (ps == -1 || pe == -1) {
      return null;
    }

    final StringTokenizer tokenizer = new StringTokenizer(text.substring(ps + 1, pe), ",");
    final int params = tokenizer.countTokens();
    @NonNls String part = tokenizer.nextToken().trim();
    if (part.endsWith("f")) {
      isFloat = true;
      fr = Float.parseFloat(part.substring(0, part.length() - 1));
    } else {
      ir = parseInt(part);
    }

    if (params >= 2) {
      part = tokenizer.nextToken().trim();
      if ("true".equals(part)) {
        alpha = true;
      } else if ("false".equals(part)) {
        alpha = false;
      } else if (part.endsWith("f")) {
        isFloat = true;
        fg = Float.parseFloat(part.substring(0, part.length() - 1));
      } else {
        ig = parseInt(part);
      }

      if (params >= 3) {
        part = tokenizer.nextToken().trim();
        if (part.endsWith("f")) {
          isFloat = true;
          fb = Float.parseFloat(part.substring(0, part.length() - 1));
        } else {
          ib = parseInt(part);
        }

        if (params == 4) {
          part = tokenizer.nextToken().trim();
          if (part.endsWith("f")) {
            isFloat = true;
            fa = Float.parseFloat(part.substring(0, part.length() - 1));
          } else {
            ia = parseInt(part);
          }
        }
      }
    }

    if (isFloat) {
      return new Color(fr, fg, fb, fa);
    } else {
      if (params == 1) {
        return new Color(ir);
      } else if (params == 2) {
        return new Color(ir, alpha);
      } else {
        return new Color(ir, ig, ib, ia);
      }
    }

  }

  private static int parseInt(final String part) {
    final int res;

    if (part.toLowerCase().startsWith("0x")) {
      res = Integer.parseInt(part.substring(2), 16);
    } else if (part.startsWith("0") && part.length() > 1) {
      res = Integer.parseInt(part.substring(1), 8);
    } else {
      res = Integer.parseInt(part);
    }

    return res;
  }

  @Override
  public Color parseColor(final String text) {
    return parseConstructor(text);
  }
}
