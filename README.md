# Color Highlighter

<h1 align="center">
  <br>
    <img src="https://raw.githubusercontent.com/mallowigi/color-highlighter/master/src/main/resources/META-INF/pluginIcon.svg?sanitize=true" alt="logo" width="200">
  <br><br>
</h1>

**Preview your colors inline!**

## Features

- Colorize different color formats directly in the code
    - `HEX` with or without hash (`#`) with alpha support
    - `rgb()`, `argb()` and `rgba()`
    - `hsl()` and `hsla()`
    - Color Tuples (`(r, g, b[, a])`)
    - Java/Kotlin/Scala -> `ColorUiResource`
    - Java/Kotlin: `[new ]Color(r, g, b[, a])`
    - Objective-C: `[NSColor colorWith...]`
    - *ASCII Colors* and *Web Colors* (https://en.wikipedia.org/wiki/Web_colors)
    - *FlatUI Colors* (https://flatuicolors.com/)
    - Custom Colors (**NEW!**)

- Supports most languages (more to come)
    - HTML
    - XML
    - YAML
    - Properties
    - JSON
    - JavaScript
    - TypeScript
    - Scala
    - Java
    - Kotlin
    - Ruby
    - Python
    - PHP
    - Go
    - ObjectiveC
    - Swift
    - C
    - C++
    - C#
    - Markdown
    - Dart
    - SQL
    - R
    - Rust (**NEW**)
    - Lua (**NEW**)
    - Toml (**NEW**)
    - Vue (**NEW**)
    - Svelte (**NEW**)
    - CSS Languages (SCSS, Less, Stylus, PostCSS)
    - Markup languages (HTML, XML, SVG, JSX, TSX, etc)
    - Text (**Experimental**)

- Different styles:
    - Background
    - Foreground
    - Border
    - Underline
    - Inline (small colored icon near the color)

- Ability to preview color in the gutter
- Options to copy gutter color in different formats (rgb, hsl, etc)
- Specify between `rgba` and `argb` detection in hex colors

- Fine-toggling:
    - Hexadecimal detection (`ff0000`, `ff0000ff`, `f0f`)
    - Color Tuples (`(r, g, b[, a])`)
    - Color names
    - Java Color Constructor (`new Color(r, g, b[, a])`)
    - Java Color methods (`Color.BLACK`, `Color.RED`)
    - Kotlin Color Constructor (`Color(r, g, b[, a])`)
    - Kotlin Color methods (`Color.BLACK`, `Color.RED`)
    - C# Color Constructor (`Color.FromArgb(r, g, b[, a])`)
    - Markdown Parsing
    - Text Parsing

## Screenshots

#### PHP

![PHP](docs/screens/php.png)

#### Python

![Python](docs/screens/python.png)

#### Ruby

![Ruby](docs/screens/ruby.png)

#### Go

![Go](docs/screens/go.png)

#### Objective C

![ObjectiveC](docs/screens/objc.png)

#### Swift

![Swift](docs/screens/swift.png)

#### JavaScript

![JavaScript](docs/screens/js.png)

#### Kotlin

![Kotlin](docs/screens/kotlin.png)

#### Java

![Java](docs/screens/java.png)

#### JSON

![JSON](docs/screens/json.png)

#### Properties

![Properties](docs/screens/properties.png)

## Acknowledgements

Plugin Icon made by [Vectors Market](https://www.flaticon.com/authors/vectors-market)
from [Flaticon](http://www.flaticon.com), licensed
by [Creative Commons BY 3.0](http://creativecommons.org/licenses/by/3.0/)

## Authors:

- [Elior Boukhobza (mallowigi)](https://github.com/mallowigi)