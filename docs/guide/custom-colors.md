# Custom Colors

Beyond the built-in palettes (Web/ASCII, FlatUI, Tailwind), Color Highlighter
lets you define **your own named colors**. This is handy for project- or
brand-specific palettes.

## Defining Custom Colors

1. Open **Settings / Preferences → Editor → Color Highlighter**.
2. Find the **Custom Colors** table.
3. Add a row with:
   - a **name** (the token that will be matched in your code), and
   - a **color value**.

Once defined, occurrences of that name are highlighted just like the built-in
named colors.

## Tips

- Choose names that are unlikely to collide with ordinary words in your code, so
  you don't get unexpected highlights. If a common word is highlighted, consider
  renaming your custom color or disabling **Detect color names**.
- Custom colors are stored in your IDE settings and apply across projects.

## Related Settings

- [Detect color names](/guide/configuration#detection-toggles) controls whether
  named colors (including your custom ones) are highlighted at all.
- See [Color Formats](/guide/color-formats#named-colors) for the built-in named
  palettes.
