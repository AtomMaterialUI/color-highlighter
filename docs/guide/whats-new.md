# What's New

The highlights below cover the most recent, notable additions. For the complete,
version-by-version history, see the [full changelog](/CHANGELOG).

## Highlighting

- **New highlighting styles** — in addition to Background, Border, Underline,
  Foreground and Inline, you can now use **Underline pill**, **Glow outline**,
  and **Disabled (Gutter only)** for gutter-only previews. See
  [Configuration](/guide/configuration#highlighting-style).
- **Rounded arc radius** — soften the corners of the Background and Border styles
  (1–10).

## Detection

- **Multiple color string literals** — several colors within a single string are
  now detected.
- **More predefined colors** added to the built-in palettes, including
  **Tailwind** colors.
- **Rust `Rgb`** constructor support.
- **Detect pure integers** option for treating raw integers as colors (opt-in).

## Languages

- New language support for **C**, **C++** and **Groovy**.
- CSS / SCSS / Less / Stylus / PostCSS and Markup (XML, HTML, SVG, JSX, TSX)
  highlighting.
- See the full list on [Supported Languages](/guide/supported-languages).

## Platform

- Migrated to the **IntelliJ Platform Gradle Plugin 2.x**.
- Support for **2026.1**.

## Fixes

- Toggling CSS support no longer changes your Editor settings.
- More precise hex detection (only 3, 6, or 8-digit values).
- Inline colors correctly appear/disappear on save.

::: tip
Something not working as expected? Check the [Gotchas](/guide/gotchas) and
[Troubleshooting](/guide/troubleshooting) pages.
:::
