# Contributing

Contributions are very welcome — whether it's a bug fix, a new language, or a
documentation improvement. This page explains how to get set up and what's
expected in a pull request.

## Prerequisites

- **JDK 25** (the project targets Java 25).
- A JetBrains IDE (IntelliJ IDEA is recommended) or just the command line.
- [Git](https://git-scm.com/) and optionally
  [pre-commit](https://pre-commit.com/) for local hooks.

The project uses the **IntelliJ Platform Gradle Plugin 2.x** and ships with a
Gradle wrapper, so you don't need to install Gradle yourself.

## Getting the Source

```sh
git clone https://github.com/mallowigi/color-highlighter.git
cd color-highlighter
```

## Running the Plugin

Launch a sandbox IDE with the plugin installed:

```sh
./gradlew runIde
```

Build the distributable plugin zip:

```sh
./gradlew buildPlugin
```

The artifact is produced under `build/distributions/`.

## Quality Checks

Before opening a PR, make sure the checks pass cleanly:

```sh
./gradlew check
```

If you only need to fix formatting issues:

```sh
./gradlew :ktlintMainSourceSetFormat
```

The repository also runs [pre-commit](https://pre-commit.com/) hooks (trailing
whitespace, YAML/JSON/XML validation, etc.). Install them once with:

```sh
pre-commit install
```

Code style is enforced with **ktlint** and **detekt** (see `detekt-config.yml`).

## Commit Messages

Commits follow the [Conventional Commits](https://www.conventionalcommits.org/)
specification (enforced by `commitlint`). For example:

```txt
feat: add color detection for <language>
fix: correct alpha channel parsing for 8-digit hex
docs: clarify the gutter copy formats
```

## Adding a New Language

The plugin was reworked to make adding languages straightforward. In broad
strokes:

1. Add a **visitor** under `src/main/java/com/mallowigi/visitors/` for the
   language's PSI.
2. Register it via the appropriate `withXxx.xml` file in
   `src/main/resources/META-INF/`.
3. If the language plugin is optional, declare it as an optional dependency.
4. Add a setting/toggle if the language needs configurable detection.

Look at an existing language (for example the Rust or Scala visitor) as a
template, and open an issue first if you're unsure about the approach.

## Pull Request Checklist

When you open a PR, please ensure:

- [ ] `./gradlew check` passes clean.
- [ ] You bumped the version (`pluginVersion` in `gradle.properties`).
- [ ] You updated the [changelog](/CHANGELOG) with your change.
- [ ] You described the change and linked any related issue.
- [ ] You added screenshots for user-facing changes where helpful.

## Reporting Issues

Found a bug or want to request a language/feature? Open an issue on the
[GitHub issue tracker](https://github.com/mallowigi/color-highlighter/issues)
with your IDE version, plugin version, language, and a small reproducer.
