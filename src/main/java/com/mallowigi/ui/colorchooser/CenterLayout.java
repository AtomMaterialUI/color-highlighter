package com.mallowigi.ui.colorchooser;

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

import java.awt.*;
import java.io.Serializable;

/**
 * Center-positioning layout manager.
 * <p>
 * This class was copied as-is from the JDK 1.5 source code.
 *
 * @author Tom Santos
 * @author Steve Wilson
 * @version 1.10 12/19/03
 */
class CenterLayout implements LayoutManager, Serializable {
  private static final long serialVersionUID = 9114506415200382600L;

  @Override
  public void addLayoutComponent(final String name, final Component comp) {
  }

  @Override
  public void removeLayoutComponent(final Component comp) {
  }

  @Override
  public Dimension preferredLayoutSize(final Container container) {
    final Component c = container.getComponent(0);
    if (c != null) {
      final Dimension size = c.getPreferredSize();
      final Insets insets = container.getInsets();
      size.width += insets.left + insets.right;
      size.height += insets.top + insets.bottom;
      return size;
    } else {
      return new Dimension(0, 0);
    }
  }

  @Override
  public Dimension minimumLayoutSize(final Container cont) {
    return preferredLayoutSize(cont);
  }

  @Override
  public void layoutContainer(final Container container) {
    try {
      final Component c = container.getComponent(0);

      c.setSize(c.getPreferredSize());
      final Dimension size = c.getSize();
      final Dimension containerSize = container.getSize();
      final Insets containerInsets = container.getInsets();
      containerSize.width -= containerInsets.left + containerInsets.right;
      containerSize.height -= containerInsets.top + containerInsets.bottom;
      int componentLeft = (containerSize.width / 2) - (size.width / 2);
      int componentTop = (containerSize.height / 2) - (size.height / 2);
      componentLeft += containerInsets.left;
      componentTop += containerInsets.top;

      c.setBounds(componentLeft, componentTop, size.width, size.height);
    } catch (final Exception e) {
      // Ignore
    }
  }
}
