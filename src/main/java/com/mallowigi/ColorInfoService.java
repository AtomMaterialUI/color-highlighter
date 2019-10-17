/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.mallowigi;

import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.Key;
import com.mallowigi.search.ColorSearchEngine;
import com.mallowigi.utils.ColorInfo;
import com.mallowigi.utils.MatchRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

enum ColorInfoService {
  ;
  // Create data holders for the highlighter, the selected range and the selected popup
  @NonNls
  private static final Key<RangeHighlighter> HIGHLIGHTER = Key.create("ColorLinkHighlighter");
  @NonNls
  private static final Key<MatchRange> LINK_RANGE = Key.create("ColorLinkRange");
  @NonNls
  private static final Key<Popup> POPUP = Key.create("ColorLinkPopup");

  @NonNls
  private static final JLabel DEFAULT_HINT = (JLabel) HintUtil.createInformationLabel("x");

  /**
   * Remove higlighters from editors
   *
   * @param editor the editor
   */
  static void clearHighlighters(final Editor editor) {
    // Remove highlighters
    final RangeHighlighter highlighter = editor.getUserData(HIGHLIGHTER);
    if (highlighter != null) {
      final MarkupModel markupModel = editor.getMarkupModel();
      markupModel.removeHighlighter(highlighter);
      editor.putUserData(HIGHLIGHTER, null);
      editor.putUserData(LINK_RANGE, null);
      editor.getContentComponent().setToolTipText(null);
    }

    clearPopup(editor);
  }

  /**
   * Clear popups
   *
   * @param editor the editor
   */
  static void clearPopup(final Editor editor) {
    final Popup popup = editor.getUserData(POPUP);
    if (popup != null) {
      popup.hide();
      editor.putUserData(POPUP, null);
    }
  }

  /**
   * Parse the line and try to find a range containing a color
   */
  @Nullable
  static MatchRange getHyperLinkRange(final Editor editor, final Point lastMouseLocation) {
    final LogicalPosition logicalPosition = editor.xyToLogicalPosition(lastMouseLocation);
    final Document document = editor.getDocument();
    final int offset = editor.logicalPositionToOffset(logicalPosition);
    final int length = document.getTextLength();

    if (length > 0 && offset < length) {
      final int start = document.getLineStartOffset(logicalPosition.line);
      final int end = document.getLineEndOffset(logicalPosition.line);
      final int pos = offset - start;
      final CharSequence text = document.getCharsSequence().subSequence(start, end + 1);

      return ColorSearchEngine.findColorMatch(text.toString(), pos, start);
    }

    return null;
  }

  /**
   * Highlight the color at the given range
   */
  public static boolean setHighlighter(final Editor editor, final MatchRange range, final Point lastMouseLocation) {
    final MatchRange current = editor.getUserData(LINK_RANGE);

    if (!range.equals(current)) {
      final Color color = ColorSearchEngine.getColor(range.getMatch());
      if (color == null) {
        return false;
      }

      final TextAttributes linkAttrs = editor.getColorsScheme().getAttributes(CodeInsightColors.HYPERLINK_ATTRIBUTES);
      final MarkupModel markupModel = editor.getMarkupModel();

      RangeHighlighter highlighter = editor.getUserData(HIGHLIGHTER);
      if (highlighter != null) {
        markupModel.removeHighlighter(highlighter);
      }

      // Add highlighting to the given range
      highlighter = markupModel.addRangeHighlighter(
          range.getStartOffset(),
          range.getEndOffset(),
          HighlighterLayer.ADDITIONAL_SYNTAX,
          linkAttrs,
          HighlighterTargetArea.EXACT_RANGE
      );
      // Save highlighted element and range
      editor.putUserData(HIGHLIGHTER, highlighter);
      editor.putUserData(LINK_RANGE, range);

      clearPopup(editor);

      // Create the popup with the color again
      final Popup popup = createPopup(editor, color, lastMouseLocation);
      popup.show();
      // Save the popup in the editor data for easy retrieval between switching tabs
      editor.putUserData(POPUP, popup);
    }

    return true;
  }

  /**
   * Generate a popup with the color
   *
   * @param editor            editor to display into
   * @param color             color to display
   * @param lastMouseLocation location to display
   * @return the popup
   */
  private static Popup createPopup(final Editor editor, final Color color, final Point lastMouseLocation) {
    // Create the popup label
    final JLabel label = new JLabel();
    label.setBorder(DEFAULT_HINT.getBorder());
    label.setOpaque(DEFAULT_HINT.isOpaque());
    label.setBackground(DEFAULT_HINT.getBackground());
    label.setForeground(DEFAULT_HINT.getForeground());
    label.setText(createColorText(DEFAULT_HINT.getFont(), color));

    // Add listeners to clear the popup on mouse move
    label.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(final MouseEvent e) {
      }

      @Override
      public void mouseMoved(final MouseEvent e) {
        clearPopup(editor);
      }
    });

    final LogicalPosition lp = editor.xyToLogicalPosition(lastMouseLocation);
    final Point pos = editor.logicalPositionToXY(lp);
    pos.x = lastMouseLocation.x;
    pos.y -= label.getPreferredSize().height;

    // Position the label
    SwingUtilities.convertPointToScreen(pos, editor.getContentComponent());
    final Rectangle rect = editor.getComponent().getRootPane().getBounds();
    final Point corner = new Point(rect.x + rect.width, 0);
    SwingUtilities.convertPointToScreen(corner, editor.getComponent().getRootPane());

    if (pos.x + label.getPreferredSize().width > corner.x) {
      pos.x = Math.max(0, corner.x - label.getPreferredSize().width);
    }
    // Create a popup in the editor with the label at the given pos
    return PopupFactory.getSharedInstance().getPopup(editor.getContentComponent(), label, pos.x, pos.y);
  }

  @SuppressWarnings({"HardCodedStringLiteral",
      "StringBufferReplaceableByString"})
  private static String createColorText(final Font font, final Color color) {
    final ColorInfo info = new ColorInfo(color);
    final StringBuilder res = new StringBuilder(100);

    // Generate the popup in html
    res.append("<html><body style=\"font: ")
       .append(font.getSize())
       .append("pt ")
       .append(font.getFamily())
       .append("\">")
       .append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\">")
       .append("<tr>")
       .append("<td style=\"border: 1 solid black background-color: ")
       .append(info.getHex())
       .append(" width: 50\">")
       .append("&nbsp")
       .append("</td>")
       .append("<td>")
       .append("<table border=\"0\" cellspacing=\"3\" cellpadding=\"0\">")
       .append("<tr>")
       .append("<td>")
       .append("<l>Hex</l>:")
       .append("</td>")
       .append("<td>")
       .append(info.getHex())
       .append("</td>")
       .append("</tr>")
       .append("<tr>")
       .append("<td>")
       .append("<l>Decimal</l>:")
       .append("</td>")
       .append("<td>")
       .append(info.getDecimalRGB())
       .append("</td>")
       .append("</tr>")
       .append("</table>")
       .append("</td>")
       .append("</tr>")
       .append("</table>")
       .append("</body></html>");

    return res.toString();
  }
}