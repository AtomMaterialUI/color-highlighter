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
 * A better GridLayout class
 *
 * This class was copied as-is from the JDK 1.5 source code.
 * @author Steve Wilson
 * @version 1.8 12/19/03
 */
class SmartGridLayout implements LayoutManager, Serializable
{
    int rows = 2;
    int columns = 2;
    int xGap = 2;
    int yGap = 2;
    int componentCount = 0;
    Component[][] layoutGrid;

    public SmartGridLayout(int numColumns, int numRows)
    {
        rows = numRows;
        columns = numColumns;
        layoutGrid = new Component[numColumns][numRows];

    }

    public void layoutContainer(Container c)
    {

        buildLayoutGrid(c);

        int[] rowHeights = new int[rows];
        int[] columnWidths = new int[columns];

        for (int row = 0; row < rows; row++)
        {
            rowHeights[row] = computeRowHeight(row);
        }

        for (int column = 0; column < columns; column++)
        {
            columnWidths[column] = computeColumnWidth(column);
        }

        Insets insets = c.getInsets();

        if (c.getComponentOrientation().isLeftToRight())
        {
            int horizLoc = insets.left;
            for (int column = 0; column < columns; column++)
            {
                int vertLoc = insets.top;

                for (int row = 0; row < rows; row++)
                {
                    Component current = layoutGrid[column][row];

                    current.setBounds(horizLoc, vertLoc, columnWidths[column], rowHeights[row]);
                    //	System.out.println(current.getBounds());
                    vertLoc += (rowHeights[row] + yGap);
                }
                horizLoc += (columnWidths[column] + xGap);
            }
        }
        else
        {
            int horizLoc = c.getWidth() - insets.right;
            for (int column = 0; column < columns; column++)
            {
                int vertLoc = insets.top;
                horizLoc -= columnWidths[column];

                for (int row = 0; row < rows; row++)
                {
                    Component current = layoutGrid[column][row];

                    current.setBounds(horizLoc, vertLoc, columnWidths[column], rowHeights[row]);
                    //	System.out.println(current.getBounds());
                    vertLoc += (rowHeights[row] + yGap);
                }
                horizLoc -= xGap;
            }
        }


    }

    public Dimension minimumLayoutSize(Container c)
    {

        buildLayoutGrid(c);
        Insets insets = c.getInsets();

        int height = 0;
        int width = 0;

        for (int row = 0; row < rows; row++)
        {
            height += computeRowHeight(row);
        }

        for (int column = 0; column < columns; column++)
        {
            width += computeColumnWidth(column);
        }

        height += (yGap * (rows - 1)) + insets.top + insets.bottom;
        width += (xGap * (columns - 1)) + insets.right + insets.left;

        return new Dimension(width, height);


    }

    public Dimension preferredLayoutSize(Container c)
    {
        return minimumLayoutSize(c);
    }

    public void addLayoutComponent(String s, Component c)
    {
    }

    public void removeLayoutComponent(Component c)
    {
    }

    private void buildLayoutGrid(Container c)
    {

        Component[] children = c.getComponents();

        for (int componentCount = 0; componentCount < children.length; componentCount++)
        {
            //	System.out.println("Children: " +componentCount);
            int row = 0;
            int column = 0;

            if (componentCount != 0)
            {
                column = componentCount % columns;
                row = (componentCount - column) / columns;
            }

            //	System.out.println("inserting into: "+ column +  " " + row);

            layoutGrid[column][row] = children[componentCount];
        }
    }

    private int computeColumnWidth(int columnNum)
    {
        int maxWidth = 1;
        for (int row = 0; row < rows; row++)
        {
            int width = layoutGrid[columnNum][row].getPreferredSize().width;
            if (width > maxWidth)
            {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private int computeRowHeight(int rowNum)
    {
        int maxHeight = 1;
        for (int column = 0; column < columns; column++)
        {
            int height = layoutGrid[column][rowNum].getPreferredSize().height;
            if (height > maxHeight)
            {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

}
