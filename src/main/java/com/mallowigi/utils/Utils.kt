/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2022 Elior "Mallowigi" Boukhobza
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

package com.mallowigi.utils

import com.dynatrace.hash4j.hashing.HashFunnel
import com.dynatrace.hash4j.hashing.Hashing
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.ColorUtil
import com.intellij.ui.svg.colorPatchedIcon
import com.mallowigi.ColorHighlighterBundle
import java.awt.Color
import javax.swing.Icon

fun getPlugin(): IdeaPluginDescriptor? = PluginManagerCore.getPlugin(PluginId.getId("com.mallowigi.colorHighlighter"))

fun getVersion(): String {
  val plugin: IdeaPluginDescriptor? = getPlugin()
  return if (plugin != null) plugin.version else ColorHighlighterBundle.message("plugin.version")
}

fun String.toHash(): Long =
  Hashing.komihash4_3().hashStream().putOrderedIterable(listOf(this), HashFunnel.forString()).asLong

/** Convert a color to a hex string. */
fun Color.toHex(): String = ColorUtil.toHex(this)

/** Get color from hex. */
fun String.fromHex(): Color = ColorUtil.fromHex(this)

@Suppress("UnstableApiUsage")
fun Icon.themedIcon(color: Color): Icon = colorPatchedIcon(this, IconColorPatcher(color))
