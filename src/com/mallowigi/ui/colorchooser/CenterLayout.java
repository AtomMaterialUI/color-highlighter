package com.maddyhome.idea.color.ui.colorchooser;

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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * Center-positioning layout manager.
 *
 * This class was copied as-is from the JDK 1.5 source code.
 * @author Tom Santos
 * @author Steve Wilson
 * @version 1.10 12/19/03
 */
class CenterLayout implements LayoutManager, Serializable
{
    public void addLayoutComponent(String name, Component comp)
    {
    }

    public void removeLayoutComponent(Component comp)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Component c = container.getComponent(0);
        if (c != null)
        {
            Dimension size = c.getPreferredSize();
            Insets insets = container.getInsets();
            size.width += insets.left + insets.right;
            size.height += insets.top + insets.bottom;
            return size;
        }
        else
        {
            return new Dimension(0, 0);
        }
    }

    public Dimension minimumLayoutSize(Container cont)
    {
        return preferredLayoutSize(cont);
    }

    public void layoutContainer(Container container)
    {
        try
        {
            Component c = container.getComponent(0);

            c.setSize(c.getPreferredSize());
            Dimension size = c.getSize();
            Dimension containerSize = container.getSize();
            Insets containerInsets = container.getInsets();
            containerSize.width -= containerInsets.left + containerInsets.right;
            containerSize.height -= containerInsets.top + containerInsets.bottom;
            int componentLeft = (containerSize.width / 2) - (size.width / 2);
            int componentTop = (containerSize.height / 2) - (size.height / 2);
            componentLeft += containerInsets.left;
            componentTop += containerInsets.top;

            c.setBounds(componentLeft, componentTop, size.width, size.height);
        }
        catch (Exception e)
        {
            // Ignore
        }
    }
}
