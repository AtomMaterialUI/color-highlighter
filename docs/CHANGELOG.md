# Changelog

## Changelog

### Features

### Fixes

### Removals

### Other

---
## [Unreleased]

## 17.0.1

### Fixes
- Fix exception when parsing Scala constructors or methods

## 17.0.0

### Features
- Add _Color constructor_ and _Color Methods_ detection for Scala (with Settings). Credits to [Ondřej Španěl](https://github.com/OndrejSpanel)
- Improve hex detection by only detecting hex colors with 3, 6 or 8 digits.

## 16.0.0

### Features
- New **Highlighting Styles**: *Background*, *Foreground*, *Border*, *Underline* and **Inline**
- Support for **Rust**
- Support for **Lua**
- Support for **Toml**
- Support for **Svelte**
- Support for **Vue**
- Migrate Settings to Kotlin UI DSL
- Add XML Visitor to be able to highlight colors inside XML nodes
- Python: Recognize tuples
- Toggle color names detection
- Toggle tuple detection

### Fixes
- Fix short rgb highlighting
- Fix deprecated usages
- Fix the display name missing error
- Make rgba format the default rather than argb
- Detect Strings in Scala

## 15.0.0

### Fixes
- Support for 2023.1

## 14.0.0

### Features
- Support for **R** and **SQL** Languages

### Fixes
- Fix the issue where the "_Use rgba instead of argb_" was doing the opposite

## 13.0.0

### Fixes
- Support for 2022.3

## 12.0.0

### Fixes
- Fix the double icon in Gutter for supported files

## 11.0.0

### Features
- **Dart** support
- Option to parse colors as `rgba` instead of `argb`
- Color Preview for _Stylesheets_ (CSS, SASS...) and _Markup_ (XML, HTML...)
- **Experimental**: Text files color parsing
- Support for numeric literals parsing (`0x...`) in JavaScript, TypeScript and Python

### Fixes
- Fix `Color.FromArgb` parsing in C#

## 9.2.0

### Fixes:
- Change hexadecimal rgba codes parsing by parsing the alpha channel on the left instead of the right

## 9.1.1
- Fix fatal error with JavaVisitor

## 9.1.0

### Fixes:
- Fix fatal error with JGoodies

## 9.0.0

### Features:
- The project has been reworked again. Now it's even easier to add new languages!
- Add Settings Page inside _Editor Settings_ to toggle highlighting on and off
- Support for Rider's languages (**C, C++, C#**)
- Support for **Markdown**
- Add finer settings to toggle specific highlightings only:
  - Toggle automatic detection of hexadecimal strings (no more coloring numbers indiscriminately)
  - Java/Kotlin `new Color()` format
  - Java/Kotlin `Color.XXX` format
  - C# `Color.FromArgb()` format
  - **Markdown** color highlighting
- **Ability to define your own colors**
- Toggle gutter's color previews from the _Gutter settings_
- Copy color to clipboard into different formats when _right clicking_ on the gutter's color preview:
  - Hexadecimal
  - `rgb` and `rgba`
  - `hsl` and `hsla`
  - Java's `new Color()` and `ColorUIResource`
  - Kotlin's `Color()` and `ColorUIResource`
  - Objective-C's `[NSColor ]`
  - Objective-C's `[UIColor ]`
  - Swift's `NSColor()`
  - Android's `Color.argb()`, `Color.rgb()`
  - C#'s `Color.FromArgb()`
- Update Notifications

### Fixes
- Remove detection of 3-5 digits as a color. No more coloring `100`, `1000` etc.
- Fix wrong detection of UUIDs as colors
- Repaint editors on save

### Other
- The project has been entirely rewritten in Kotlin (except for setting pages)
- Color Highlighting Settings are feature dependent (no Kotlin settings if the plugin is off)
- Work on the documentation

## 8.0.0

### Other
- Convert project to Kotlin

## 7.0.0

### Bump
- Support for 2021.1

## 6.0.0

### Bump
- Upgrade dependencies

## 5.0.0

### Bump
- Support to 2021.1

## 4.0.0

### Bump
- Support for 2020.3

## 3.0.0

### Features
- XML
- HTML
- JavaScript
- TypeScript
- Properties
- YAML
- Python
- Ruby
- Java (ColorUiResource)
- Kotlin (ColorUiResource)
- Scala (ColorUiResource)
- Go
- Objective C
- Swift

## 2.0.0

### Bump
- Upgrade to 2019.1

## 1.0.0

### Bump
- First version of the plugin
