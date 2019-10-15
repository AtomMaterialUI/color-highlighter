package com.maddyhome.idea.color;

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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.maddyhome.idea.color.ui.ColorBrowserToolWindow;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This plugin provides the Color Browser Tool Window.
 */
public class ColorBrowserPluginImpl implements ColorBrowserPlugin, JDOMExternalizable
{
    public ColorBrowserPluginImpl(Project project)
    {
        logger.debug("ctr");

        this.project = project;

        toolWindow = new ColorBrowserToolWindow(project);

        URL resource = getClass().getResource("/resources/logo16x16.png");
        if (resource != null)
        {
            icon = new ImageIcon(resource);
        }
    }

    @NonNls
    public String getComponentName()
    {
        return "ColorBrowser";
    }

    public void initComponent()
    {
        logger.debug("initComponent");
        toolWindow.initComponent();
    }

    public void disposeComponent()
    {
        logger.debug("disposeComponent");
    }

    public void projectOpened()
    {
        logger.debug("projectOpened");
        ToolWindowManager manager = ToolWindowManager.getInstance(project);
        window = manager.registerToolWindow(TITLE, toolWindow, ToolWindowAnchor.LEFT);
        window.setIcon(icon);
    }

    public void projectClosed()
    {
        ToolWindowManager manager = ToolWindowManager.getInstance(project);
        manager.unregisterToolWindow(TITLE);
    }

    public void readExternal(Element element) throws InvalidDataException
    {
        logger.debug("readExternal");
        toolWindow.readExternal(element);
    }

    public void writeExternal(Element element) throws WriteExternalException
    {
        logger.debug("writeExternal");
        toolWindow.writeExternal(element);
    }

    public void showAtCursor()
    {
        if (!window.isVisible())
        {
            window.show(null);
        }
        toolWindow.showAtCursor();
    }

    private Project project;
    private ColorBrowserToolWindow toolWindow;
    private ToolWindow window;
    private Icon icon = null;

    private static final String TITLE = "Color Browser";

    private static Logger logger = Logger.getInstance(ColorBrowserPluginImpl.class.getName());
}