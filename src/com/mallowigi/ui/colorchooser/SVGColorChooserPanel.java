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

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.maddyhome.idea.color.config.Configuration;
import com.maddyhome.idea.color.util.ColorInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * Implements a tab for a JColorChooser. This provides two drop down lists. The lists contain the SVG colors
 * defined by w3c.org. One list is alphabetical and one is by code.
 */
public class SVGColorChooserPanel extends AbstractColorChooserPanel
{
    public void updateChooser()
    {
        Color col = getColorFromModel();
        int code = col.getRGB() & 0xffffff;
        String name = Configuration.getInstance().findSVGName(col);
        if (name != null)
        {
            boxNames.setSelectedItem(name);
            boxColors.setSelectedItem(code);
        }
        else
        {
            boxNames.setSelectedIndex(-1);
            boxColors.setSelectedIndex(-1);
        }
    }

    protected void buildChooser()
    {
        Map<String,Integer> names = Configuration.getInstance().getSVGNames();
        Object[] items = new Object[names.size()];
        Iterator iterator = names.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            items[i] = iterator.next();
        }

        boxNames = new JComboBox(items);
        boxNames.setRenderer(new ColorListCellRenderer());
        boxNames.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent)
            {
                String name = (String)boxNames.getSelectedItem();
                if (name != null)
                {
                    Color color = Configuration.getInstance().findSVGColor(name);
                    if (color != null)
                    {
                        getColorSelectionModel().setSelectedColor(color);
                    }
                }
            }
        });

        Map<Integer,String> colors = Configuration.getInstance().getSVGColors();
        items = new Object[colors.size()];
        iterator = colors.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            items[i] = iterator.next();
        }

        boxColors = new JComboBox(items);
        boxColors.setRenderer(new ColorListCellRenderer());
        boxColors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent)
            {
                Integer code = (Integer)boxColors.getSelectedItem();
                if (code != null)
                {
                    Color color = new Color(code);
                    getColorSelectionModel().setSelectedColor(color);
                }
            }
        });

        setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        add(new JLabel("Alphabetical:"), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        add(boxNames, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        add(new JLabel("Color Code:"), new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        add(boxColors, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    }

    public String getDisplayName()
    {
        return "SVG Colors";
    }

    public Icon getSmallDisplayIcon()
    {
        return null;
    }

    public Icon getLargeDisplayIcon()
    {
        return null;
    }

    private static class ColorListCellRenderer extends JPanel implements ListCellRenderer
    {
        public ColorListCellRenderer()
        {
            setLayout(new BorderLayout());
            label = new JLabel("MMMMMMMMMMMMMMMMM");
            add(BorderLayout.CENTER, label);
            color = new JPanel();
            Dimension dim = label.getPreferredSize();
            dim.width = dim.height;
            color.setPreferredSize(dim);
            add(BorderLayout.WEST, color);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
        {
            String text;
            Color col;
            if (value == null)
            {
                text = "";
                col = list.getBackground();
            }
            else if (value instanceof String)
            {
                text = (String)value;
                col = Configuration.getInstance().findSVGColor(text);
                text = String.format(ColorInfo.isHexUppercase() ? "%s - #%06X" : "%s - #%06x", text, col.getRGB() & 0xffffff);
            }
            else
            {
                col = new Color((Integer)value);
                text = String.format(ColorInfo.isHexUppercase() ? "#%06X - %s" : "#%06x - %s", col.getRGB() & 0xffffff, Configuration.getInstance().findSVGName(col));
            }

            label.setText(text);

            if (isSelected)
            {
                setForeground(list.getSelectionForeground());
                setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setBackground(list.getSelectionBackground());
            }
            else
            {
                setForeground(list.getForeground());
                setBackground(list.getBackground());
                label.setForeground(list.getForeground());
                label.setBackground(list.getBackground());
            }

            color.setBackground(col);

            return this;
        }

        private JPanel color;
        private JLabel label;
    }

    private JComboBox boxNames;
    private JComboBox boxColors;
}
