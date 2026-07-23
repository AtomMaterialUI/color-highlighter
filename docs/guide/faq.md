# FAQ

## Which IDEs are supported?

Any JetBrains IDE built on the IntelliJ Platform — IntelliJ IDEA, WebStorm,
PyCharm, PhpStorm, RubyMine, GoLand, Rider, CLion, DataGrip and more. Some
languages additionally require their own language plugin to be installed.

## Is it free?

Yes. Color Highlighter is open source and released under the MIT License.

## Does it change my files?

No. Highlighting is purely visual — it renders overlays in the editor and adds
gutter icons. It never modifies your source. The only time it writes anything is
when you explicitly copy a color or insert one via the color picker.

## Why isn't my color highlighted?

Most often it's a detection toggle or a digit-count rule. See
[Troubleshooting](/guide/troubleshooting) and [Gotchas](/guide/gotchas).

## Can I define my own colors?

Yes — see [Custom Colors](/guide/custom-colors).

## How do I change the way colors look?

Pick a [highlighting style](/guide/configuration#highlighting-style)
(Background, Foreground, Border, Underline, or Inline) in the settings.

## Can I convert a color from one format to another?

Yes. Right-click the [gutter](/guide/gutter) swatch and copy the color in the
format you need (hex, rgb/rgba, hsl/hsla, language-specific constructors, etc.).

## How do I report a bug or request a language?

Open an issue on the
[GitHub repository](https://github.com/mallowigi/color-highlighter/issues) with
details and, ideally, a small reproducer.
