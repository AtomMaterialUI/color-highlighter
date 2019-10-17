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

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mallowigi.colors.ColorsService;
import com.mallowigi.utils.ColorInfo;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

/**
 * Implements a tab for a JColorChooser. This provides two drop down lists. The lists contain the SVG colors
 * defined by w3c.org. One list is alphabetical and one is by code.
 */
public class SVGColorChooserPanel extends AbstractColorChooserPanel {
  private static final long serialVersionUID = 4456307110743708468L;
  private JComboBox boxNames;
  private JComboBox boxColors;

  @Override
  public void updateChooser() {
    final Color col = getColorFromModel();
    final int code = col.getRGB() & 0xffffff;
    final String name = ColorsService.getInstance().findSVGName(col);
    if (name != null) {
        boxNames.setSelectedItem(name);
        boxColors.setSelectedItem(code);
    } else {
        boxNames.setSelectedIndex(-1);
        boxColors.setSelectedIndex(-1);
    }
  }

  @Override
  protected void buildChooser() {
    final Map<String, Integer> names = ColorsService.getInstance().getSVGNames();
    Object[] items = new Object[names.size()];
    Iterator iterator = names.keySet().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      items[i] = iterator.next();
    }

      boxNames = new JComboBox(items);
      boxNames.setRenderer(new ColorListCellRenderer());
      boxNames.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        final String name = (String) boxNames.getSelectedItem();
        if (name != null) {
          final Color color = ColorsService.getInstance().findSVGColor(name);
          if (color != null) {
              getColorSelectionModel().setSelectedColor(color);
          }
        }
      }
    });

    final Map<Integer, String> colors = ColorsService.getInstance().getSVGColors();
    items = new Object[colors.size()];
    iterator = colors.keySet().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      items[i] = iterator.next();
    }

      boxColors = new JComboBox(items);
      boxColors.setRenderer(new ColorListCellRenderer());
      boxColors.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        final Integer code = (Integer) boxColors.getSelectedItem();
        if (code != null) {
          final Color color = new Color(code);
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

  @Override
  public String getDisplayName() {
    return "SVG Colors";
  }

  @Override
  public Icon getSmallDisplayIcon() {
    return null;
  }

  @Override
  public Icon getLargeDisplayIcon() {
    return null;
  }

  private static class ColorListCellRenderer extends JPanel implements ListCellRenderer {
    private static final long serialVersionUID = 8507060861911053432L;
    private final JPanel color;
    private final JLabel label;

    ColorListCellRenderer() {
        setLayout(new BorderLayout());
        label = new JLabel("MMMMMMMMMMMMMMMMM");
        add(BorderLayout.CENTER, label);
        color = new JPanel();
      final Dimension dim = label.getPreferredSize();
      dim.width = dim.height;
        color.setPreferredSize(dim);
        add(BorderLayout.WEST, color);
    }

    @Override
    public Component getListCellRendererComponent(final JList list,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean hasFocus) {
      String text;
      final Color col;
      if (value == null) {
        text = "";
        col = list.getBackground();
      } else if (value instanceof String) {
        text = (String) value;
        col = ColorsService.getInstance().findSVGColor(text);
        text = String.format(ColorInfo.isHexUppercase() ? "%s - #%06X" : "%s - #%06x", text, col.getRGB() & 0xffffff);
      } else {
        col = new Color((Integer) value);
        text = String.format(ColorInfo.isHexUppercase() ? "#%06X - %s" : "#%06x - %s", col.getRGB() & 0xffffff,
          ColorsService.getInstance().findSVGName(col));
      }

        label.setText(text);

      if (isSelected) {
          setForeground(list.getSelectionForeground());
          setBackground(list.getSelectionBackground());
          label.setForeground(list.getSelectionForeground());
          label.setBackground(list.getSelectionBackground());
      } else {
          setForeground(list.getForeground());
          setBackground(list.getBackground());
          label.setForeground(list.getForeground());
          label.setBackground(list.getBackground());
      }

        color.setBackground(col);

      return this;
    }
  }
}
