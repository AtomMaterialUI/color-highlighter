# Gutter & Copying

In addition to inline highlights, Color Highlighter shows a color swatch in the
editor **gutter** next to each detected color.

## Gutter Previews

A small colored icon appears in the gutter for every recognized color. The icons
have a subtle border for better contrast against the editor background.

You can toggle gutter previews on or off from the **Gutter settings**. If you
prefer previews **only** in the gutter (and not inline), that combination is
supported too.

## Copying Colors

**Right-click** a gutter color icon to copy the color to the clipboard in a
variety of formats:

- **Hexadecimal**
- `rgb` and `rgba`
- `hsl` and `hsla`
- Java — `new Color(...)` and `ColorUIResource`
- Kotlin — `Color(...)`, Color RGB, and `ColorUIResource`
- Objective-C — `[NSColor …]` and `[UIColor …]` (including HSB / HSBA variants)
- Swift — `NSColor(...)`
- Android — `Color.argb(...)`, `Color.rgb(...)`
- C# — `Color.FromArgb(...)`

This makes it easy to convert a color from one representation to another: pick
the color once, then copy it in whatever format your current file needs.

## Picking a Color

You can also open a color picker (**Choose Color…**) to select a new color
value, which is inserted in the appropriate format for the file you're editing.
