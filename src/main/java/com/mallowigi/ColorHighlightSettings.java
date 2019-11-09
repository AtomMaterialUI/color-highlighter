/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Elior Boukhobza
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

import com.intellij.application.options.editor.WebEditorOptions;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.BeanConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NonNls;

public final class ColorHighlightSettings extends BeanConfigurable<WebEditorOptions> implements UnnamedConfigurable {
  @NonNls
  private static final String COLOR_HIGHLIGHT = "COLOR_HIGHLIGHT";

  public ColorHighlightSettings() {
    super(WebEditorOptions.getInstance());
    if (PropertiesComponent.getInstance().getValue(COLOR_HIGHLIGHT) == null) {
      PropertiesComponent.getInstance().setValue(COLOR_HIGHLIGHT, true);
    }

    checkBox("ColorHighlighter: Show colors inline",
      ColorHighlightSettings::isPluginEnabled,
      ColorHighlightSettings::applySettings);
  }

  static boolean isPluginEnabled() {
    return PropertiesComponent.getInstance().getBoolean(COLOR_HIGHLIGHT);
  }

  private static void applySettings(final boolean value) {
    PropertiesComponent.getInstance().setValue(COLOR_HIGHLIGHT, value);
  }

  @Override
  public void apply() throws ConfigurationException {
    super.apply();

    final Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
    for (final Project project : openProjects) {
      DaemonCodeAnalyzer.getInstance(project).restart();
    }
  }
}
