package com.mallowigi.search;

import com.google.common.collect.Lists;
import com.mallowigi.colors.ColorsService;
import com.mallowigi.utils.EditorSelection;
import com.mallowigi.utils.MTColorUtils;
import com.mallowigi.utils.MatchRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ColorSearchEngine {
  ;
  public static final char SEMICOLON = ';';
  public static final String RGB = "rgb";
  public static final String HSL = "hsl";
  public static final String COLOR_METHOD = "Color.";
  public static final String COLOR_UIRESOURCE_METHOD = "ColorUIResource.";
  public static final String COLOR = "Color";
  public static final String COLOR_UI_RESOURCE = "ColorUIResource";
  // region COLOR_PATTERNS
  private static final List<Pattern> COLOR_PATTERNS = Collections.unmodifiableList(
      Lists.newArrayList(
          Pattern.compile(
              "((#\\p{XDigit}{6}\\b)|(#\\p{XDigit}{3}\\b))"
          ),
          // #123456 or #333
          Pattern.compile(
              "\\b((rgb\\s*\\(\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}\\s*\\))|(rgb\\s*\\(\\s*\\p{Digit}{1," +
                  "3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*\\)))"
          ),
          // rgb(128, 128, 128)
          Pattern.compile(
              "\\b((rgba\\s*\\(\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}\\s*,\\s*[0-9.]{1,3}\\s*\\))|" +
                  "(rgba\\s*\\" +
                  "(\\s*\\p{Digit}{1,3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*[0-9.]{1,3}\\s*\\)))"
          ),
          // rgba(128, 128, 128, 0)
          Pattern.compile(
              "\\b(hsl\\s*\\(\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*\\))"
          ),
          // hsl(0, 12, 120)
          Pattern.compile(
              "\\b(hsla\\s*\\(\\s*\\p{Digit}{1,3}\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*\\p{Digit}{1,3}%\\s*,\\s*[0-9.]{1,3}\\s*\\))"
          ),
          // hsla(0, 12, 120, 1)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
          ),
          // Color(0x123456)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(true|false)\\s*\\))"
          ),
          // Color(0x123456, true)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
          ),
          // Color(128, 0, 12)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])" +
                  "?[0-9a-fA-F]+\\s*\\))"
          ),
          // Color(123, 0, 12, 12)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
          ),
          // Color(12f, 32f, 9f)
          Pattern.compile(
              "\\b(Color\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
          ),
          // Color(12f, 32f, 9f, 0f)
          Pattern.compile(
              "\\b(Color\\.[a-zA-Z_]+)\\b"
          ),
          // Color.yellow
          Pattern.compile(
              "\\b(ColorUIResource\\s*\\(\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*,\\s*[0-9.]+f?\\s*\\))"
          ),
          // ColorUIResource(12f, 12f, 12f)
          Pattern.compile(
              "\\b(ColorUIResource\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
          ),
          // ColorUIResource(0x123456)
          Pattern.compile(
              "\\b(ColorUIResource\\s*\\(\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*,\\s*(0[xX])?[0-9a-fA-F]+\\s*\\))"
          ),
          // ColorUIResource(12f, 12f, 12f)
          Pattern.compile(
              "\\b(ColorUIResource\\.[a-zA-Z_]+)\\b"
          ),
          // ColorUIResource.black
          Pattern.compile(
              "\\b([a-zA-Z]+)\\b"
          )
          // aliceblue
      ));
  // endregion

  /**
   * Find a color pattern in a line
   *
   * @param line   the editor line
   * @param p      the cursor position
   * @param offset optional offset
   * @return a match range
   */
  @Nullable
  public static MatchRange findColorMatch(@NotNull final CharSequence line, final int p, final int offset) {
    int pos = p;
    if (pos > 0 && line.charAt(pos) == SEMICOLON) {
      pos--;
    }

    for (final Pattern pattern : COLOR_PATTERNS) {
      final Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        if (matcher.start() <= pos && pos < matcher.end()) {
          return new MatchRange(matcher.start() + offset, matcher.end() + offset, matcher.group());
        }
      }
    }

    return null;
  }

  /**
   * Return search results of a color in a selection
   *
   * @param selection a selection
   * @return list of colors
   */
  @NotNull
  public static SearchResults findColor(@NotNull final EditorSelection selection) {
    final String line = selection.getLine();
    final int selectionStart = selection.getStart();
    final int selectionEnd = selection.getEnd();

    // If there is a selection only parse the selection
    if (selectionStart != selectionEnd) {
      final String highlight = line.substring(selectionStart, selectionEnd);
      final Color color = getColor(highlight);

      if (color != null) {
        final SearchResults searchResults = new SearchResults(selection);
        searchResults.addColor(color);
        return searchResults;
      }
    }

    // Find a color in the current line
    final SearchResults searchResults = new SearchResults(selection);
    final MatchRange range = findColorMatch(line, selectionStart, 0);

    if (range != null) {
      final Color color = getColor(range.getMatch());
      if (color != null) {
        searchResults.addColor(color);
      }
    }

    return searchResults;
  }

  /**
   * Try to parse a color using the provided formats
   * TODO use Factory
   */
  @Nullable
  private static Color getColor(@NotNull final String text) {
    final Color color;
    try {
      if (text.startsWith("#")) {
        color = MTColorUtils.parseHex(text);
      } else if (text.startsWith(RGB)) {
        color = MTColorUtils.parseRGB(text);
      } else if (text.startsWith(HSL)) {
        color = MTColorUtils.parseHSL(text);
      } else if (text.startsWith(COLOR_METHOD)) {
        color = MTColorUtils.parseMethod(text, COLOR_METHOD);
      } else if (text.startsWith(COLOR_UIRESOURCE_METHOD)) {
        color = MTColorUtils.parseMethod(text, COLOR_UIRESOURCE_METHOD);
      } else if (text.startsWith(COLOR) || text.startsWith(COLOR_UI_RESOURCE)) {
        return MTColorUtils.parseConstructor(text);
      } else {
        color = ColorsService.getInstance().findSVGColor(text.toLowerCase());
      }
      return color;

    } catch (final NumberFormatException e) {
      return null;
    }
  }

}