package com.mallowigi.utils;

/*
 * Color Browser - Color browser plugin for IDEA
 * Copyright (C) 2006 Rick Maddy. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.mallowigi.search.ColorSearchEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditorUtil {
  private EditorUtil() {
  }

  @Nullable
  public static EditorSelection getCursorSelection(@NotNull final Project project) {
    final FileEditorManager manager = FileEditorManager.getInstance(project);
    final Editor editor = manager.getSelectedTextEditor();
    if (editor != null) {
      final int caret = editor.getCaretModel().getOffset();
      int start = editor.getSelectionModel().getSelectionStart();
      int end = editor.getSelectionModel().getSelectionEnd();
      final Document doc = editor.getDocument();
      final int lline = editor.offsetToLogicalPosition(caret).line;
      final int lstart = doc.getLineStartOffset(lline);
      final int lend = doc.getLineEndOffset(lline);

      final String line = doc.getCharsSequence().subSequence(lstart, lend).toString();
      start -= lstart;
      end -= lstart;

      if (start < line.length()) {
        return new EditorSelection(line, lstart, start, end);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public static void refocusEditor(@NotNull final Project project) {
    final FileEditorManager manager = FileEditorManager.getInstance(project);
    final Editor editor = manager.getSelectedTextEditor();
    if (editor != null) {
      final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
      manager.openFile(file, true);
    }
  }

  public static void insertColor(@NotNull final String text, @NotNull final Project project) {
    final FileEditorManager manager = FileEditorManager.getInstance(project);
    final Editor editor = manager.getSelectedTextEditor();
    if (editor != null) {
      final Document doc = editor.getDocument();
      if (doc.isWritable() || FileDocumentManager.fileForDocumentCheckedOutSuccessfully(doc, project)) {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
          @Override
          public void run() {
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
              @Override
              public void run() {
                int start = editor.getSelectionModel().getSelectionStart();
                int end = editor.getSelectionModel().getSelectionEnd();
                if (start == end) {
                  final EditorSelection sel = getCursorSelection(project);
                  if (sel != null) {
                    final MatchRange range = ColorSearchEngine.findColorMatch(sel.getLine(), sel.getStart(), 0);
                    if (range != null) {
                      start = sel.getOffset() + range.getStartOffset();
                      end = sel.getOffset() + range.getEndOffset();
                    }
                  }
                }
                if (start != end) {
                  doc.replaceString(start, end, text);
                } else {
                  doc.insertString(start, text);
                  editor.getCaretModel().moveCaretRelatively(text.length(), 0, false, false,
                    false);
                }
              }
            });
          }
        }, "ColorBrowserInsert", null);
      }
    }
  }
}