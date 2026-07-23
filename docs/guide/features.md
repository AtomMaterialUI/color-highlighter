# Features

Color Highlighter previews colors inline across a wide range of languages and
formats. Here is what it can do.

## Inline Color Previews

Colors written in your code are rendered directly in the editor so you can see
them at a glance. The way they appear depends on the
[highlighting style](/guide/configuration#highlighting-style) you pick.

## Multiple Highlighting Styles

Choose how colors are displayed:

| Style          | Description                                              |
| -------------- | -------------------------------------------------------- |
| **Background** | Paints the color behind the text.                        |
| **Foreground** | Paints the text itself in the color.                     |
| **Border**     | Draws a border around the color value.                   |
| **Underline**  | Underlines the color value.                              |
| **Inline**     | Renders a small colored icon (swatch) next to the value. |

For the **Background** and **Border** styles you can also set a
[rounded arc radius](/guide/configuration#highlighting-style) (1–10) for softer
corners.

## Gutter Previews

A color swatch is shown in the editor gutter next to each detected color. You
can [copy the color in many formats](/guide/gutter) by right-clicking it, or
disable gutter icons entirely.

## Rich Format Support

Color Highlighter recognizes a lot of color notations. See the full list on the
[Color Formats](/guide/color-formats) page. Highlights include:

- `HEX` with or without a leading `#`, including alpha.
- `rgb()`, `rgba()` and `argb()`.
- `hsl()` and `hsla()`.
- Color tuples such as `(r, g, b[, a])`.
- Language constructors like Java/Kotlin `Color(r, g, b[, a])`, `ColorUIResource`,
  Objective-C `[NSColor colorWith…]`, C# `Color.FromArgb(…)`, and Rust `Rgb(…)`.
- Named colors: **ASCII / Web colors**, **FlatUI colors**, and **Tailwind colors**.
- Your own [Custom Colors](/guide/custom-colors).

## Broad Language Coverage

Dozens of languages are supported, from stylesheets and markup to Java, Kotlin,
Rust, Swift, Go, Python, PHP and more. See
[Supported Languages](/guide/supported-languages) for the full list.

## Fine-grained Detection Toggles

Every detection kind can be toggled independently so nothing is highlighted by
accident — hex strings, tuples, color names, and each language's color
constructors/properties. See [Configuration](/guide/configuration).

## Screenshots

A few examples across languages:

### CSS / Stylesheets

```css
.card {
  background: #2ecc71;
  color: rgba(0, 0, 0, 0.87);
}
```

### PHP

![PHP](../screens/php.png)

### Python

![Python](../screens/python.png)

### JavaScript

![JavaScript](../screens/js.png)

### Kotlin

![Kotlin](../screens/kotlin.png)

### Java

![Java](../screens/java.png)

### Go

![Go](../screens/go.png)

### Swift

![Swift](../screens/swift.png)

### Objective-C

![Objective-C](../screens/objc.png)
