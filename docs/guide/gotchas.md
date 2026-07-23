# Gotchas

A few behaviors are intentional but can be surprising. Knowing them up front
saves confusion.

## Hex Detection is Limited to 3, 6 or 8 Digits

To avoid painting arbitrary numbers, only hex values with **3, 6, or 8 digits**
are treated as colors. This means:

- `100`, `1000`, and other 4–5 digit numbers are **not** highlighted.
- UUID-like strings are not mistaken for colors.

If a value you expect to be a color isn't highlighted, check its digit count.

## rgba vs. argb Ambiguity

An 8-digit hex value can't declare where its alpha channel lives. Color
Highlighter uses **rgba** (`rrggbbaa`) by default. If your alpha looks inverted
or wrong, toggle **Use rgba instead of argb** in
[Configuration](/guide/configuration#hex-parsing).

## "Detect pure integers" is Greedy

Enabling **Detect pure integers** will highlight numbers like `255` or
`16777215` as colors. Because integers appear everywhere, this can produce a lot
of noise. Leave it **off** unless you specifically need it.

## Color Names Can Match Ordinary Words

With **Detect color names** enabled, words such as `red`, `blue`, or a
[custom color](/guide/custom-colors) name may be highlighted even in prose or
identifiers. Disable the toggle, or pick less common custom-color names, if this
bothers you.

## Text Parsing is Experimental

Highlighting colors in plain **text** files is experimental and off by default.
It may over- or under-match. Enable **Parse Colors in Text Files** only if you
accept the trade-off.

## Language Support Depends on IDE Plugins

Some languages only work when the matching language plugin is installed and
enabled. For example, PHP highlighting needs the PHP plugin (bundled in
PhpStorm, installable in IntelliJ IDEA Ultimate). If an entire language isn't
highlighting, verify its plugin is active. See
[Troubleshooting](/guide/troubleshooting).

## CSS Toggle vs. Editor Settings

Toggling CSS support only affects Color Highlighter — it does **not** change your
IDE's own Editor settings. (This was a bug in older versions and has since been
fixed.)

## Highlights Refresh on Save

Inline colors are repainted when the file is saved. If a highlight seems stale
after an edit, saving the file forces a refresh.
