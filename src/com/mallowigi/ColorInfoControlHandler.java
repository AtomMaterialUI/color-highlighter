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

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

final class ColorInfoControlHandler implements KeyListener, EditorMouseMotionListener, EditorMouseListener, VisibleAreaListener {
  private final Editor editor;
  private final Point lastMouseLocation = new Point(0, 0);

  ColorInfoControlHandler(final Editor editor) {
    this.editor = editor;
  }

  /**
   * Clear popups on key pressed
   */
  @Override
  public void keyTyped(final KeyEvent e) {
    ColorInfoService.clearPopup(editor);
  }

  /**
   * Clear popups on key pressed
   */
  @Override
  public void keyPressed(final KeyEvent e) {
    ColorInfoService.clearPopup(editor);
  }

  /**
   * Clear highlighters unless ctrl is pressed
   */
  @Override
  public void keyReleased(final KeyEvent e) {
    if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != InputEvent.CTRL_DOWN_MASK) {
      ColorInfoService.clearHighlighters(editor);

      editor.getContentComponent().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }
  }

  @Override
  public void mouseMoved(@NotNull final EditorMouseEvent e) {
    //    final MouseEvent mouseEvent = e.getMouseEvent();
    //    lastMouseLocation.setLocation(mouseEvent.getPoint());
    //
    //    // if ctrl is pressed
    //    if ((e.getMouseEvent().getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
    //      final Editor editor = e.getEditor();
    //      final MatchRange range = ColorInfoService.getHyperLinkRange(editor, lastMouseLocation);
    //      if (range != null) {
    //        if (setHighlighter(editor, range)) {
    //          editor.getContentComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    //        }
    //      }
    //    }
  }

  @Override
  public void mouseDragged(@NotNull final EditorMouseEvent e) {
    // no-op
  }

  @Override
  public void mousePressed(@NotNull final EditorMouseEvent event) {
    // no-op
  }

  @Override
  public void mouseClicked(final EditorMouseEvent event) {
    //    if (!e.isConsumed() &&
    //        (e.getMouseEvent().getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK &&
    //        e.getMouseEvent().getButton() == MouseEvent.BUTTON1) {
    //      final Editor editor = e.getEditor();
    //      final MatchRange range = getHyperLinkRange(editor);
    //      if (range != null) {
    //        final Color color = SearchEngine.getColor(range.getMatch());
    //        if (color != null) {
    //          final Project project = editor.getProject();
    //          if (project != null) {
    //            final ColorBrowserPlugin plugin = project.getComponent(ColorBrowserPlugin.class);
    //            plugin.showAtCursor();
    //          }
    //        }
    //      }
    //    }

    ColorInfoService.clearHighlighters(event.getEditor());
  }

  @Override
  public void mouseReleased(@NotNull final EditorMouseEvent event) {
    // no-op
  }

  @Override
  public void mouseEntered(@NotNull final EditorMouseEvent event) {
    // no-op
  }

  @Override
  public void mouseExited(@NotNull final EditorMouseEvent event) {
    // no-op
  }

  /**
   * Clear higlighters when scrolling change
   */
  @Override
  public void visibleAreaChanged(@NotNull final VisibleAreaEvent e) {
    ColorInfoService.clearHighlighters(editor);
  }

}
