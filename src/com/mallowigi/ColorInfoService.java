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

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.util.Key;
import com.mallowigi.search.ColorSearchEngine;
import com.mallowigi.utils.MatchRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

enum ColorInfoService {
  ;
  // Create data holders for the highlighter, the selected range and the selected popup
  @NonNls
  private static final Key<RangeHighlighter> HIGHLIGHTER = Key.create("ColorLinkHighlighter");
  @NonNls
  private static final Key<MatchRange> LINK_RANGE = Key.create("ColorLinkRange");
  @NonNls
  private static final Key<Popup> POPUP = Key.create("ColorLinkPopup");

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
}