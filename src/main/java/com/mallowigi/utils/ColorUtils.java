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

package com.mallowigi.utils;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Color Utils!
 */
@SuppressWarnings({"StandardVariableNames",
  "OverlyComplexBooleanExpression",
  "SingleCharacterStartsWith",
  "AssignmentToMethodParameter",
  "FloatingPointEquality",
  "MethodWithMultipleReturnPoints"})
public enum ColorUtils {
  DEFAULT;

  public static final char OPEN_PAREN = '(';
  public static final char CLOSE_PAREN = ')';
  @NonNls
  public static final String COMMA = ",";
  @NonNls
  public static final String PERCENT = "%";

  /**
   * Parse rgb in the hex format #123
   */
  @NotNull
  public static Color getShortRGB(final String hex) {
    final String rgb = normalizeRGB(hex, 3);

    final int r = Integer.parseInt(rgb.substring(0, 1), 16);
    final int g = Integer.parseInt(rgb.substring(1, 2), 16);
    final int b = Integer.parseInt(rgb.substring(2, 3), 16);

    return new Color(r, g, b);
  }

  @NonNls
  private static String padZeros(final int num, final String color) {
    return StringUtil.repeat("0", num) + color;
  }

  /**
   * Parse rgb in the hex format #123456
   */
  @NotNull
  public static Color getRGB(final String hex) {
    final String rgb = normalizeRGB(hex, 6);

    final int r = Integer.parseInt(rgb.substring(0, 2), 16);
    final int g = Integer.parseInt(rgb.substring(2, 4), 16);
    final int b = Integer.parseInt(rgb.substring(4, 6), 16);

    return new Color(r, g, b);
  }

  @NotNull
  private static String normalizeRGB(final String hex, final int i) {
    String rgb = hex;
    if (rgb.length() < i) {
      rgb = padZeros(i - rgb.length(), rgb);
    } else if (rgb.length() > i) {
      rgb = rgb.substring(0, i);
    }
    return rgb;
  }

  /**
   * Parse a color from r,g,b in decimal format
   */
  public static Color getDecimalRGB(final int r, final int g, final int b) {
    return getDecimalRGBa(r, g, b, 1.0f);
  }

  /**
   * Parse a color from r,g,b,a in decimal format
   */
  public static Color getDecimalRGBa(int r, int g, int b, final float a) {
    r = normalizeDecimal(r);
    g = normalizeDecimal(g);
    b = normalizeDecimal(b);
    final int x = toDecimal(normalizeFraction(a));

    return new Color(r, g, b, x);
  }

  /**
   * Parse a color from r,g,b in percent format
   */
  public static Color getPercentRGB(final int r, final int g, final int b) {
    return getPercentRGBa(r, g, b, 1.0f);
  }

  /**
   * Parse a color from r,g,b,a in percent format
   */
  public static Color getPercentRGBa(int r, int g, int b, float a) {
    r = normalizePercent(r);
    g = normalizePercent(g);
    b = normalizePercent(b);
    a = normalizeFraction(a);

    return new Color(r / 100.0f, g / 100.0f, b / 100.0f, a);
  }

  /**
   * Parse a color from h,s,l in decimal format
   */
  public static Color getHSL(final int h, final int s, final int l) {
    return getHSLa(h, s, l, 1.0f);
  }

  /**
   * Parse a color from h,s,l,a in decimal format
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static Color getHSLa(int h, int s, int l, float a) {
    h = normalizeDegrees(h);
    s = normalizePercent(s);
    l = normalizePercent(l);
    a = normalizeFraction(a);

    final float[] rgb = convertHSLToRGB(h / 359.0f, s / 100.0f, l / 100.0f);

    return new Color(rgb[0], rgb[1], rgb[2], a);
  }

  /**
   * Converts hsl to rgb
   */
  public static int HSLtoRGB(final float h, final float s, final float l) {
    final float[] vals = convertHSLToRGB(h, s, l);

    final int r = (int) (vals[0] * 255.0f + 0.5f);
    final int g = (int) (vals[1] * 255.0f + 0.5f);
    final int b = (int) (vals[2] * 255.0f + 0.5f);

    return (255 << 24) | (r << 16) | (g << 8) | b;
  }

  /**
   * Get a color from hsl
   */
  public static Color getHSLColor(final float h, final float s, final float l) {
    return new Color(HSLtoRGB(h, s, l));
  }

  /**
   * Converts rgb to hsl
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static float[] RGBtoHSL(final int r, final int g, final int b, float[] hsl) {
    if (hsl == null) {
      hsl = new float[3];
    }

    final float[] vals = convertRGBToHSL(r, g, b);

    hsl[0] = vals[0];
    hsl[1] = vals[1];
    hsl[2] = vals[2];

    return hsl;
  }

  private static float[] convertHSLToRGB(final float h, final float s, final float l) {
    final float[] res = new float[3];

        /* From w3c.org
        HOW TO RETURN hsl.to.rgb(h, s, l):
            SELECT:
                l<=0.5: PUT l*(s+1) IN m2
                ELSE: PUT l+s-l*s IN m2
            PUT l*2-m2 IN m1
            PUT hue.to.rgb(m1, m2, h+1/3) IN r
            PUT hue.to.rgb(m1, m2, h    ) IN g
            PUT hue.to.rgb(m1, m2, h-1/3) IN l
            RETURN (r, g, l)

        HOW TO RETURN hue.to.rgb(m1, m2, h):
            IF h<0: PUT h+1 IN h
            IF h>1: PUT h-1 IN h
            IF h*6<1: RETURN m1+(m2-m1)*h*6
            IF h*2<1: RETURN m2
            IF h*3<2: RETURN m1+(m2-m1)*(2/3-h)*6
            RETURN m1
        */

    final float m2 = (l <= 0.5f) ? l * (s + 1.0f) : l + s - l * s;
    final float m1 = l * 2.0f - m2;

    res[0] = convertHueToRGB(m1, m2, h + 1.0f / 3.0f);
    res[1] = convertHueToRGB(m1, m2, h);
    res[2] = convertHueToRGB(m1, m2, h - 1.0f / 3.0f);

    return res;
  }

  @SuppressWarnings({"AssignmentToMethodParameter",
    "MethodWithMultipleReturnPoints"})
  private static float convertHueToRGB(final float m1, final float m2, float h) {
    if (h < 0.0f) {
      h++;
    }
    if (h > 1.0f) {
      h--;
    }
    if (h * 6.0f < 1.0f) {
      return m1 + (m2 - m1) * h * 6.0f;
    }
    if (h * 2.0f < 1.0f) {
      return m2;
    }
    if (h * 3.0f < 2.0f) {
      return m1 + (m2 - m1) * (2.0f / 3.0f - h) * 6.0f;
    }
    return m1;
  }

  private static float[] convertRGBToHSL(final int r, final int g, final int b) {
    final float[] res = new float[3];

        /*
        var_R = ( R / 255 )                     //Where RGB values = 0 � 255
        var_G = ( G / 255 )
        var_B = ( B / 255 )

        var_Min = min( var_R, var_G, var_B )    //Min. value of RGB
        var_Max = max( var_R, var_G, var_B )    //Max. value of RGB
        del_Max = var_Max - var_Min             //Delta RGB value

        L = ( var_Max + var_Min ) / 2

        if ( del_Max == 0 )                     //This is a gray, no chroma...
        {
           H = 0                                //HSL results = 0 � 1
           S = 0
        }
        else                                    //Chromatic data...
        {
           if ( L < 0.5 ) S = del_Max / ( var_Max + var_Min )
           else           S = del_Max / ( 2 - var_Max - var_Min )

           del_R = ( ( ( var_Max - var_R ) / 6 ) + ( del_Max / 2 ) ) / del_Max
           del_G = ( ( ( var_Max - var_G ) / 6 ) + ( del_Max / 2 ) ) / del_Max
           del_B = ( ( ( var_Max - var_B ) / 6 ) + ( del_Max / 2 ) ) / del_Max

           if      ( var_R == var_Max ) H = del_B - del_G
           else if ( var_G == var_Max ) H = ( 1 / 3 ) + del_R - del_B
           else if ( var_B == var_Max ) H = ( 2 / 3 ) + del_G - del_R

           if ( H < 0 ) ; H += 1
           if ( H > 1 ) ; H -= 1
        }
        */

    final float vR = r / 255.0f;
    final float vG = g / 255.0f;
    final float vB = b / 255.0f;

    final float min = Math.min(vR, Math.min(vG, vB));
    final float max = Math.max(vR, Math.max(vG, vB));
    final float delMax = max - min;

    final float l = (max + min) / 2.0f;
    float h;
    final float s;

    if (delMax == 0.0f) {
      h = 0.0f;
      s = 0.0f;
    } else {
      s = l < 0.5f ? delMax / (max + min) : delMax / (2.0f - max - min);

      final float dR = (((max - vR) / 6.0f) + (delMax / 2.0f)) / delMax;
      final float dG = (((max - vG) / 6.0f) + (delMax / 2.0f)) / delMax;
      final float dB = (((max - vB) / 6.0f) + (delMax / 2.0f)) / delMax;

      if (vR == max) {
        h = dB - dG;
      } else if (vG == max) {
        h = (1.0f / 3.0f) + dR - dB;
      } else /* if (vB == max) */ {
        h = (2.0f / 3.0f) + dG - dR;
      }

      if (h < 0.0f) {
        h++;
      }
      if (h > 1.0f) {
        h--;
      }
    }

    res[0] = h;
    res[1] = s;
    res[2] = l;

    return res;
  }

  /**
   * Ensure a number is between 0 and 255
   */
  private static int normalizeDecimal(final int x) {
    return Math.max(0, Math.min(x, 255));
  }

  /**
   * Ensure a number is between 0 and 100
   */
  private static int normalizePercent(final int x) {
    return Math.max(0, Math.min(x, 100));
  }

  /**
   * Ensure a number is between 0 and 360
   */
  private static int normalizeDegrees(final int x) {
    return Math.max(0, Math.min(x % 360, 359));
  }

  /**
   * Ensure a number is between 0 and 1
   */
  private static float normalizeFraction(final float f) {
    return Math.max(0.0f, Math.max(f, 1.0f));
  }

  /**
   * Converts to decimal
   */
  private static int toDecimal(final float f) {
    return (int) (f * 255.0f + 0.5f);
  }

}
