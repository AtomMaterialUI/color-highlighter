# Troubleshooting

If colors aren't showing up the way you expect, work through the checks below.

## Nothing is Highlighted at All

1. **Is the plugin enabled?** Go to **Settings → Editor → Color Highlighter** and
   make sure **Enable** is checked. When it's off, all sub-settings are hidden.
2. **Is the plugin installed and up to date?** Check **Settings → Plugins →
   Installed** for "Color Highlighter" and update if an update is available.
3. **Restart the IDE** after installing or updating.

## A Specific Format Isn't Highlighted

- **Hex not detected?** Confirm it has [3, 6, or 8 digits](/guide/gotchas#hex-detection-is-limited-to-3-6-or-8-digits).
- **Tuples not detected?** Enable **Detect tuples**.
- **Color names not detected?** Enable **Detect color names**.
- **Constructor/method not detected?** Enable the matching language toggle (e.g.
  Java **Detect Color Constructor**, C# **Detect Color.FromArgb**, Rust **Detect
  Rust Rgb**). See [Configuration](/guide/configuration#language-specific-settings).

## A Whole Language Isn't Highlighted

Some languages require their language plugin to be installed and enabled (PHP,
Go, Ruby, Rust, Scala, Dart, Lua, R, Vue, Svelte, etc.). Check **Settings →
Plugins** and ensure the relevant plugin is active. See
[Gotchas](/guide/gotchas#language-support-depends-on-ide-plugins).

## Alpha / Transparency Looks Wrong

Toggle **Use rgba instead of argb** in
[Configuration](/guide/configuration#hex-parsing). 8-digit hex values are
ambiguous about which channel is the alpha.

## Too Many Things Are Highlighted

- Turn **off** **Detect pure integers** — it's greedy by design.
- Turn **off** **Detect color names** if ordinary words are being colored.
- Disable **Parse Colors in Text Files** (experimental) if plain text over-matches.

See [Gotchas](/guide/gotchas) for the reasoning behind these defaults.

## Highlights Look Stale

Inline highlights refresh on save — save the file to force a repaint. If it
persists, try **File → Invalidate Caches / Restart…**.

## Start Fresh

If your settings are in a strange state, use **Reset Defaults** on the settings
page to restore everything to its original values.

## Still Stuck?

Open an issue with your IDE version, plugin version, language, and a small code
sample on the
[GitHub issue tracker](https://github.com/mallowigi/color-highlighter/issues).
