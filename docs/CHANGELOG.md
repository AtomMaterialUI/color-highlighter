# Changelog

## [Unreleased]

---

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
``
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

This plugin has been reworked from the ground up, and therefore *tries* to support all languages as much as possible. But currently it fully supports:
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

More to come!

## 2.0.0

### Bump

- Upgrade to 2019.1

## 1.0.0

### Bump

- First version of the plugin
