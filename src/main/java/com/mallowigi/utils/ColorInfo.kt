package com.mallowigi.utils;

import com.mallowigi.colors.ColorsService;

import java.awt.*;

@SuppressWarnings("ClassWithTooManyFields")
public final class ColorInfo {
  private static boolean hexUppercase;
  private final String txtRGB;
  private final String txtHSB;
  private final String txtHSL;
  private final String shortHex;
  private final String hex;
  private final String decimalRGB;
  private final String percentRGB;
  private final String decimalRGBa;
  private final String percentRGBa;
  private final String hsl;
  private final String hsla;
  private final String colorFFF;
  private final String colorFFFF;
  private final String colorI;
  private final String colorIB;
  private final String colorIII;
  private final String colorIIII;
  private final String colorUIResourceFFF;
  private final String colorUIResourceI;
  private final String colorUIResourceIII;
  private final Color color;

  @SuppressWarnings("HardCodedStringLiteral")
  public ColorInfo(final Color color) {
    this.color = color;
    final int argb = color.getRGB();
    final int rgb = argb & 0xffffff;
    final float[] comps = color.getRGBComponents(null);
    final float[] hslComps = ColorUtils.RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), null);
    final float[] hsbComps = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

    txtRGB = String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue());
    txtHSB = String.format("%d, %d%%, %d%%", toDegrees(hsbComps[0]), toPercent(hsbComps[1]), toPercent(hsbComps[2]));
    txtHSL = String.format("%d, %d%%, %d%%", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]));

    shortHex = String.format(hexUppercase ? "#%1X%1X%1X" : "#%1x%1x%1x", color.getRed() / 16, color.getGreen() / 16,
      color.getBlue() / 16);
    hex = String.format(hexUppercase ? "#%06X" : "#%06x", rgb);
    decimalRGB = String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    percentRGB = String.format("rgb(%d%%, %d%%, %d%%)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]));
    decimalRGBa = String.format("rgba(%d, %d, %d, %.2f)", color.getRed(), color.getGreen(), color.getBlue(), comps[3]);
    percentRGBa = String.format("rgba(%d%%, %d%%, %d%%, %.2f)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]), comps[3]);
    hsl = String.format("hsl(%d, %d%%, %d%%)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]));
    hsla = String.format("hsla(%d, %d%%, %d%%, %.2f)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]), comps[3]);
    colorFFF = String.format("Color(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2]);
    colorFFFF = String.format("Color(%.2ff, %.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2], comps[3]);
    colorI = String.format(hexUppercase ? "Color(0x%06X)" : "Color(0x%06x)", rgb);
    colorIB = String.format(hexUppercase ? "Color(0x%08X, true)" : "Color(0x%08x, true)", argb);
    colorIII = String.format("Color(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    colorIIII = String.format("Color(%d, %d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    colorUIResourceFFF = String.format("ColorUIResource(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2]);
    colorUIResourceI = String.format(hexUppercase ? "ColorUIResource(0x%06X)" : "ColorUIResource(0x%06x)", rgb);
    colorUIResourceIII = String.format("ColorUIResource(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
  }

  public static boolean isHexUppercase() {
    return hexUppercase;
  }

  public static void setHexUppercase(final boolean hexUppercase) {
    ColorInfo.hexUppercase = hexUppercase;
  }

  private static int toPercent(final float val) {
    return Math.round(val * 100);
  }

  private static int toDegrees(final float val) {
    return Math.round(val * 359);
  }

  public String getTxtRGB() {
    return txtRGB;
  }

  public String getTxtHSB() {
    return txtHSB;
  }

  public String getTxtHSL() {
    return txtHSL;
  }

  public String getShortHex() {
    return shortHex;
  }

  public String getHex() {
    return hex;
  }

  public String getSVGName() {
    return ColorsService.getInstance().findSVGName(color);
  }

  public String getJavaName() {
    return ColorsService.getInstance().findJavaName(color);
  }

  public Color getColor() {
    return color;
  }

  public String getDecimalRGB() {
    return decimalRGB;
  }

  public String getPercentRGB() {
    return percentRGB;
  }

  public String getDecimalRGBa() {
    return decimalRGBa;
  }

  public String getPercentRGBa() {
    return percentRGBa;
  }

  public String getHsl() {
    return hsl;
  }

  public String getHsla() {
    return hsla;
  }

  public String getColorFFF() {
    return colorFFF;
  }

  public String getColorFFFF() {
    return colorFFFF;
  }

  public String getColorI() {
    return colorI;
  }

  public String getColorIB() {
    return colorIB;
  }

  public String getColorIII() {
    return colorIII;
  }

  public String getColorIIII() {
    return colorIIII;
  }

  public String getColorUIResourceFFF() {
    return colorUIResourceFFF;
  }

  public String getColorUIResourceI() {
    return colorUIResourceI;
  }

  public String getColorUIResourceIII() {
    return colorUIResourceIII;
  }
}
