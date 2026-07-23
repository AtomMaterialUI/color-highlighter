# Supported Languages

Color Highlighter supports a large and growing set of languages. Support for
some languages depends on the corresponding IDE plugin being installed (for
example, PHP support requires the PHP plugin, which ships with PhpStorm).

## Stylesheets & Markup

- CSS and CSS-family languages: **SCSS**, **Less**, **Stylus**, **PostCSS**
- **HTML**
- **XML** / **SVG**
- **JSX** / **TSX**

## Programming Languages

| Language        | Notes                                                         |
| --------------- | ------------------------------------------------------------- |
| **JavaScript**  | Also numeric literals (`0x…`)                                 |
| **TypeScript**  | Also numeric literals (`0x…`)                                 |
| **Java**        | `Color(…)`, `Color.RED`, `ColorUIResource`                    |
| **Kotlin**      | `Color(…)`, `Color.RED`, `ColorUIResource`                    |
| **Scala**       | Color constructor and color methods                           |
| **Groovy**      |                                                               |
| **C**           |                                                               |
| **C++**         |                                                               |
| **C#**          | `Color.FromArgb(…)`                                           |
| **Objective-C** | `[NSColor colorWith…]`, `[UIColor …]`                         |
| **Swift**       | `NSColor(…)`                                                  |
| **Go**          |                                                               |
| **Rust**        | `Rgb(…)` constructor                                          |
| **Python**      | Tuples and numeric literals (`0x…`)                           |
| **Ruby**        |                                                               |
| **PHP**         |                                                               |
| **Dart**        | Including references to color values                          |
| **Lua**         |                                                               |
| **R**           |                                                               |

## Data & Config Formats

- **JSON**
- **YAML**
- **Properties**
- **TOML**
- **SQL**

## Frameworks & Templating

- **Vue**
- **Svelte**

## Documentation & Text

- **Markdown** — colors inside Markdown files (toggleable)
- **Text** — plain text color parsing (**experimental**, see
  [Gotchas](/guide/gotchas))

::: tip Missing a language?
New languages are added regularly. If your language isn't listed, open an issue
on the [GitHub repository](https://github.com/mallowigi/color-highlighter/issues).
:::
