package com.maddyhome.idea.color.util;

/*
 * Color Browser - Color browser plugin for IDEA
 * Copyright (C) 2006 Rick Maddy. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.maddyhome.idea.color.config.Configuration;

import java.awt.Color;

public class ColorInfo
{
    public ColorInfo(Color color)
    {
        this.color = color;
        int argb = color.getRGB();
        int rgb = argb & 0xffffff;
        float[] comps = color.getRGBComponents(null);
        float[] hslComps = ColorUtil.RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] hsbComps = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        txtRGB = String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue());
        txtHSB = String.format("%d, %d%%, %d%%", toDegrees(hsbComps[0]), toPercent(hsbComps[1]), toPercent(hsbComps[2]));
        txtHSL = String.format("%d, %d%%, %d%%", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]));

        shortHex = String.format(isHexUppercase() ? "#%1X%1X%1X" : "#%1x%1x%1x", color.getRed() / 16, color.getGreen() / 16, color.getBlue() / 16);
        hex = String.format(isHexUppercase() ? "#%06X" : "#%06x", rgb);
        decimalRGB = String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
        percentRGB = String.format("rgb(%d%%, %d%%, %d%%)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]));
        decimalRGBa = String.format("rgba(%d, %d, %d, %.2f)", color.getRed(), color.getGreen(), color.getBlue(), comps[3]);
        percentRGBa = String.format("rgba(%d%%, %d%%, %d%%, %.2f)", toPercent(comps[0]), toPercent(comps[1]), toPercent(comps[2]), comps[3]);
        hsl = String.format("hsl(%d, %d%%, %d%%)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]));
        hsla = String.format("hsla(%d, %d%%, %d%%, %.2f)", toDegrees(hslComps[0]), toPercent(hslComps[1]), toPercent(hslComps[2]), comps[3]);
        colorFFF = String.format("Color(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2]);
        colorFFFF = String.format("Color(%.2ff, %.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2], comps[3]);
        colorI = String.format(isHexUppercase() ? "Color(0x%06X)" : "Color(0x%06x)", rgb);
        colorIB = String.format(isHexUppercase() ? "Color(0x%08X, true)" : "Color(0x%08x, true)", argb);
        colorIII = String.format("Color(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
        colorIIII = String.format("Color(%d, %d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        colorUIResourceFFF = String.format("ColorUIResource(%.2ff, %.2ff, %.2ff)", comps[0], comps[1], comps[2]);
        colorUIResourceI = String.format(isHexUppercase() ? "ColorUIResource(0x%06X)" : "ColorUIResource(0x%06x)", rgb);
        colorUIResourceIII = String.format("ColorUIResource(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public String getTxtRGB()
    {
        return txtRGB;
    }

    public String getTxtHSB()
    {
        return txtHSB;
    }

    public String getTxtHSL()
    {
        return txtHSL;
    }

    public String getShortHex()
    {
        return shortHex;
    }

    public String getHex()
    {
        return hex;
    }

    public String getSVGName()
    {
        return Configuration.getInstance().findSVGName(color);
    }

    public String getJavaName()
    {
        return Configuration.getInstance().findJavaName(color);
    }

    public Color getColor()
    {
        return color;
    }

    public String getDecimalRGB()
    {
        return decimalRGB;
    }

    public String getPercentRGB()
    {
        return percentRGB;
    }

    public String getDecimalRGBa()
    {
        return decimalRGBa;
    }

    public String getPercentRGBa()
    {
        return percentRGBa;
    }

    public String getHsl()
    {
        return hsl;
    }

    public String getHsla()
    {
        return hsla;
    }

    public String getColorFFF()
    {
        return colorFFF;
    }

    public String getColorFFFF()
    {
        return colorFFFF;
    }

    public String getColorI()
    {
        return colorI;
    }

    public String getColorIB()
    {
        return colorIB;
    }

    public String getColorIII()
    {
        return colorIII;
    }

    public String getColorIIII()
    {
        return colorIIII;
    }

    public String getColorUIResourceFFF()
    {
        return colorUIResourceFFF;
    }

    public String getColorUIResourceI()
    {
        return colorUIResourceI;
    }

    public String getColorUIResourceIII()
    {
        return colorUIResourceIII;
    }

    private int toPercent(float val)
    {
        return Math.round(val * 100);
    }

    private int toDegrees(float val)
    {
        return Math.round(val * 359);
    }

    public static boolean isHexUppercase()
    {
        return hexUppercase;
    }

    public static void setHexUppercase(boolean hexUppercase)
    {
        ColorInfo.hexUppercase = hexUppercase;
    }

    private String txtRGB;
    private String txtHSB;
    private String txtHSL;

    private String shortHex;
    private String hex;
    private String decimalRGB;
    private String percentRGB;
    private String decimalRGBa;
    private String percentRGBa;
    private String hsl;
    private String hsla;
    private String colorFFF;
    private String colorFFFF;
    private String colorI;
    private String colorIB;
    private String colorIII;
    private String colorIIII;
    private String colorUIResourceFFF;
    private String colorUIResourceI;
    private String colorUIResourceIII;
    private Color color;

    private static boolean hexUppercase;
}
