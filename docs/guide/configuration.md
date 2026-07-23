# Configuration

All of Color Highlighter's options live in the IDE settings. Open
**Settings / Preferences → Editor → Color Highlighter**.

## Global Settings

### Enable

The master switch. When disabled, no colorizing happens and the feature-specific
settings are hidden.

> Color Highlighting settings are feature-dependent: if the plugin is off, the
> related settings (including language-specific pages) are hidden.

### Highlighting Style

Select how colors are rendered in the editor:

- **Background** — paint the color behind the text.
- **Foreground** — paint the text in the color.
- **Border** — draw a border around the value.
- **Underline** — underline the value.
- **Inline** — show a small colored swatch next to the value.

### Rounded arc radius

For the **Background** and **Border** styles, sets the rounded-corner radius
(**1–10**) for softer edges.

## Detection Toggles

Fine-grained switches that control what is recognized, so nothing is highlighted
by accident:

| Setting                   | What it does                                                         |
| ------------------------- | ------------------------------------------------------------------- |
| **Detect hex colors**     | Highlight values that look like hex colors (e.g. `ff00ff`).         |
| **Detect color names**    | Highlight words that look like color names (`red`, `blue`, …).      |
| **Detect tuples**         | Highlight numeric tuples such as `(128, 10, 255)`.                  |
| **Detect pure integers**  | Highlight pure integers as colors (e.g. `255`, `16777215`). Greedy. |

## Hex Parsing

### Use rgba instead of argb

Controls how 8-digit hex strings are parsed:

- **Off (default is rgba)** — see the note below.
- **On** — parse hex strings as `rgba` (`rrggbbaa`) instead of `argb`
  (`aarrggbb`).

::: tip
As of recent versions the default is `rgba`. If your alpha channel looks wrong,
toggle this setting.
:::

## CSS & Markup

- **Highlight CSS Colors** — highlight colors in CSS and markup files (XML,
  HTML, etc.).
- **Parse Colors in XML Nodes** — also highlight colors inside XML nodes, not
  just attributes.

## Markdown & Text

- **Parse Colors in Markdown Files** — highlight colors in Markdown.
- **Parse Colors in Text Files** — highlight colors in plain text files
  (**experimental**).

## Language-specific Settings

Several languages have their own detection switches, grouped by language:

### Java

- **Detect Color Constructor** — `new Color(...)`.
- **Detect Color Properties** — `Color.red`, `Color.BLACK`, …

### Kotlin

- **Detect Color Constructor** — `Color(...)`.
- **Detect Color Properties** — `Color.red`, `Color.BLACK`, …

### Scala / Groovy

- Color constructor and color property detection.

### Rider (C#)

- **Detect Color.FromArgb** — highlight the `Color.FromArgb(...)` method call.

### Rust

- **Detect Rust Rgb** — highlight the `Rgb(...)` constructor.

## Gutter

Color previews in the gutter are controlled from the **Gutter settings** and can
be toggled on or off. See [Gutter & Copying](/guide/gutter).

## Reset Defaults

Use **Reset Defaults** to restore every option to its original value. You will
be asked to confirm before the reset is applied.
