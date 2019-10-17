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
 * A better GridLayout class
 * <p>
 * This class was copied as-is from the JDK 1.5 source code.
 *
 * @author Steve Wilson
 * @version 1.8 12/19/03
 */
class SmartGridLayout implements LayoutManager, Serializable {
  private static final long serialVersionUID = -7579838037673731570L;
  int componentCount = 0;
  private int rows = 2;
  private int columns = 2;
  private final int xGap = 2;
  private final int yGap = 2;
  private final Component[][] layoutGrid;

  SmartGridLayout(final int numColumns, final int numRows) {
      rows = numRows;
      columns = numColumns;
      layoutGrid = new Component[numColumns][numRows];

  }

  @Override
  public void addLayoutComponent(final String s, final Component c) {
  }

  @Override
  public void removeLayoutComponent(final Component c) {
  }

  @Override
  public Dimension preferredLayoutSize(final Container c) {
    return minimumLayoutSize(c);
  }

  @Override
  public Dimension minimumLayoutSize(final Container c) {

      buildLayoutGrid(c);
    final Insets insets = c.getInsets();

    int height = 0;
    int width = 0;

    for (int row = 0; row < rows; row++) {
      height += computeRowHeight(row);
    }

    for (int column = 0; column < columns; column++) {
      width += computeColumnWidth(column);
    }

    height += (yGap * (rows - 1)) + insets.top + insets.bottom;
    width += (xGap * (columns - 1)) + insets.right + insets.left;

    return new Dimension(width, height);

  }

  @Override
  public void layoutContainer(final Container c) {

      buildLayoutGrid(c);

    final int[] rowHeights = new int[rows];
    final int[] columnWidths = new int[columns];

    for (int row = 0; row < rows; row++) {
      rowHeights[row] = computeRowHeight(row);
    }

    for (int column = 0; column < columns; column++) {
      columnWidths[column] = computeColumnWidth(column);
    }

    final Insets insets = c.getInsets();

    if (c.getComponentOrientation().isLeftToRight()) {
      int horizLoc = insets.left;
      for (int column = 0; column < columns; column++) {
        int vertLoc = insets.top;

        for (int row = 0; row < rows; row++) {
          final Component current = layoutGrid[column][row];

          current.setBounds(horizLoc, vertLoc, columnWidths[column], rowHeights[row]);
          //	System.out.println(current.getBounds());
          vertLoc += (rowHeights[row] + yGap);
        }
        horizLoc += (columnWidths[column] + xGap);
      }
    } else {
      int horizLoc = c.getWidth() - insets.right;
      for (int column = 0; column < columns; column++) {
        int vertLoc = insets.top;
        horizLoc -= columnWidths[column];

        for (int row = 0; row < rows; row++) {
          final Component current = layoutGrid[column][row];

          current.setBounds(horizLoc, vertLoc, columnWidths[column], rowHeights[row]);
          //	System.out.println(current.getBounds());
          vertLoc += (rowHeights[row] + yGap);
        }
        horizLoc -= xGap;
      }
    }

  }

  private void buildLayoutGrid(final Container c) {

    final Component[] children = c.getComponents();

    for (int componentCount = 0; componentCount < children.length; componentCount++) {
      //	System.out.println("Children: " +componentCount);
      int row = 0;
      int column = 0;

      if (componentCount != 0) {
        column = componentCount % columns;
        row = (componentCount - column) / columns;
      }

      //	System.out.println("inserting into: "+ column +  " " + row);

        layoutGrid[column][row] = children[componentCount];
    }
  }

  private int computeColumnWidth(final int columnNum) {
    int maxWidth = 1;
    for (int row = 0; row < rows; row++) {
      final int width = layoutGrid[columnNum][row].getPreferredSize().width;
      if (width > maxWidth) {
        maxWidth = width;
      }
    }
    return maxWidth;
  }

  private int computeRowHeight(final int rowNum) {
    int maxHeight = 1;
    for (int column = 0; column < columns; column++) {
      final int height = layoutGrid[column][rowNum].getPreferredSize().height;
      if (height > maxHeight) {
        maxHeight = height;
      }
    }
    return maxHeight;
  }

}
