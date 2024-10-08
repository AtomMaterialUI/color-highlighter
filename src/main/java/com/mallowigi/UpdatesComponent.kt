/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2023 Elior "Mallowigi" Boukhobza
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
package com.mallowigi

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.mallowigi.config.home.ColorHighlighterState
import com.mallowigi.utils.getVersion

/**
 * Represents a component responsible for handling updates in the ColorHighlighter plugin.
 *
 * This class extends the [ProjectActivity] class.
 */
class UpdatesComponent : ProjectActivity {
  private var config: ColorHighlighterState = ColorHighlighterState.instance

  override suspend fun execute(project: Project) = onProjectOpened(project)

  private fun projectOpened(project: Project) {
    // Show new version notification
    val pluginVersion = getVersion()
    // val updated = pluginVersion != config.version
    config.version = pluginVersion

    // if (updated) {
    //   ColorHighlighterNotifications.showUpdate(project)
    // }
  }

  private fun onProjectOpened(project: Project) {
    config = ColorHighlighterState.instance
    projectOpened(project)
  }
}
