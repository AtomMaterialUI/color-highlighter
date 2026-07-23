# Color Formats

Color Highlighter understands many ways of expressing a color. This page lists
the recognized notations.

::: info
You can disable specific formats in the settings.
:::

## Hexadecimal

Hex colors are detected with **3, 6, or 8 digits**, with or without a leading
`#`:

```txt
#f0f          // 3 digits (shorthand)
#ff00ff       // 6 digits
#ff00ff80     // 8 digits (with alpha)
ff00ff        // no hash
```

::: warning 3, 6 or 8 digits only
To avoid highlighting random numbers, only 3, 6 and 8-digit values are treated
as hex colors. Values like `100` or `1000` are intentionally ignored. See
[Gotchas](/guide/gotchas).
:::

::: info
They are enabled for all languages by default, but you can disable them in the settings.
:::

### rgba vs. argb

An 8-digit hex value is ambiguous: is the alpha channel first (`aarrggbb`) or
last (`rrggbbaa`)? Use the **Use rgba instead of argb** setting to choose. See
[Configuration](/guide/configuration#hex-parsing).

## Functional Notations

```css
rgb(52, 152, 219)
rgba(52, 152, 219, 0.5)
argb(0.5, 52, 152, 219)
hsl(204, 70%, 53%)
hsla(204, 70%, 53%, 0.5)
```

## Lab / Lch

The plugin also supports the Mathematical color spaces.

```css
lab(50, 20, 30)
lch(50, 20, 30)
oklab(50, 20, 30)
oklch(50, 20, 30)
hwb(204, 70%, 53%)
```

## Color Tuples

Bare tuples of numbers are recognized as colors when tuple detection is enabled:

```py
(128, 10, 255)
(128, 10, 255, 128)   # with alpha
```

::: tip
Tuple detection is a toggle — see [Configuration](/guide/configuration#detection-toggles).
:::

## Named Colors

- **ASCII / Web colors** — the standard [web color names](https://en.wikipedia.org/wiki/Web_colors)
  such as `red`, `blue`, `rebeccapurple`.
- **FlatUI colors** — from [flatuicolors.com](https://flatuicolors.com/).
- **Tailwind colors** — Tailwind's palette (matched without prefixes/suffixes).

Color-name detection is a toggle so ordinary words aren't accidentally
highlighted.

## Language Constructors & Properties

Depending on the language, Color Highlighter can also detect specific proprietary formats:

| Language    | Constructor / Method                           |
|-------------|------------------------------------------------|
| Java        | `new Color(r, g, b[, a])`, `ColorUIResource`   |
| Java        | `Color.BLACK`, `Color.RED` (properties)        |
| Kotlin      | `Color(r, g, b[, a])`, `ColorUIResource`       |
| Kotlin      | `Color.BLACK`, `Color.RED` (properties)        |
| Scala       | `new Color(…)`, `Color.RED`                    |
| JS/TS       | `.color(r, g, b[, a])`, `.color(h, s, l[, a])` |
| C#          | `Color.FromArgb(r, g, b[, a])`                 |
| Objective-C | `[NSColor colorWith…]`, `[UIColor …]`          |
| Swift       | `NSColor(…)`                                   |
| Rust        | `Rgb(…)`                                       |

Each of these is individually toggleable in
[Configuration](/guide/configuration#language-specific-settings).

## Pure Integers

Optionally, pure integers can be interpreted as colors (for example `255` or
`16777215`). This is **off by default** because it is very greedy — enable
**Detect pure integers** only when you need it.
