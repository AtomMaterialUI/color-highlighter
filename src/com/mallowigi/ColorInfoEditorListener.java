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
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;

import java.util.Map;
import java.util.WeakHashMap;

final class ColorInfoEditorListener implements EditorFactoryListener {
  // Link editor to control handlers
  private final Map<Editor, ColorInfoControlHandler> keyHandlers = new WeakHashMap<>(3);

  @Override
  public void editorCreated(final EditorFactoryEvent event) {
    final Editor editor = event.getEditor();
    final ColorInfoControlHandler controlHandler = new ColorInfoControlHandler(editor);
    keyHandlers.put(editor, controlHandler);

    // Listen to mouse overs, editions and scrolling events
    editor.addEditorMouseMotionListener(controlHandler);
    editor.addEditorMouseListener(controlHandler);
    editor.getContentComponent().addKeyListener(controlHandler);
    editor.getScrollingModel().addVisibleAreaListener(controlHandler);
  }

  @Override
  public void editorReleased(final EditorFactoryEvent event) {
    final Editor editor = event.getEditor();
    final ColorInfoControlHandler controlHandler = keyHandlers.remove(editor);

    editor.removeEditorMouseMotionListener(controlHandler);
    editor.removeEditorMouseListener(controlHandler);
    editor.getContentComponent().removeKeyListener(controlHandler);
    editor.getScrollingModel().removeVisibleAreaListener(controlHandler);
  }

}
