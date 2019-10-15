package com.maddyhome.idea.color.ui;

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
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.maddyhome.idea.color.search.SearchEngine;
import com.maddyhome.idea.color.search.SearchResults;
import com.maddyhome.idea.color.ui.colorchooser.HSLColorChooserPanel;
import com.maddyhome.idea.color.ui.colorchooser.HexColorChooserPanel;
import com.maddyhome.idea.color.ui.colorchooser.JavaColorChooserPanel;
import com.maddyhome.idea.color.ui.colorchooser.SVGColorChooserPanel;
import com.maddyhome.idea.color.util.EditorSelection;
import com.maddyhome.idea.color.util.EditorUtil;
import org.jdom.Element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorBrowserToolWindow extends JPanel implements JDOMExternalizable
{

    public ColorBrowserToolWindow(final Project project)
    {
        logger.debug("ctr");
        this.project = project;

        setupControls();
        logger.debug("ctr-done");
    }

    public void showAtCursor()
    {
        selectColor();
    }

    private void setColor(Color color)
    {
        logger.debug("setColor(" + color + ")");
        if (color != null)
        {
            chooser.setColor(color);
            detailPanel.setColor(color);
        }
        else
        {
            detailPanel.disable();
        }

        lastColor = color;
    }

    public void resetColor()
    {
        setColor(lastColor);
    }

    private void selectColor()
    {
        EditorSelection sel = EditorUtil.getCursorSelection(project);

        if (sel != null)
        {
            SearchResults results = SearchEngine.findColor(sel);
            Map<Integer,Color> colors = results.getColors();
            if (!colors.isEmpty())
            {
                Collection<Color> coll = colors.values();
                for (Color color : coll)
                {
                    setColor(color);
                }
            }
            else
            {
                setColor(null);
            }
        }
    }

    private void setupControls()
    {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, pnlMain);

        pnlDetail.setLayout(new BorderLayout());

        detailPanel = new DetailPanel(project, this);
        pnlDetail.add(BorderLayout.CENTER, detailPanel.getMainComponent());
        detailPanel.disable();

        pnlChooser.setLayout(new BorderLayout());

        chooser = new JColorChooser();
        setupChooser();

        pnlChooser.add(BorderLayout.CENTER, chooser);
    }

    private void setupChooser()
    {
        chooser.addChooserPanel(new HSLColorChooserPanel());
        chooser.addChooserPanel(new HexColorChooserPanel());
        chooser.addChooserPanel(new SVGColorChooserPanel());
        chooser.addChooserPanel(new JavaColorChooserPanel());

        chooser.getSelectionModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent)
            {
                detailPanel.setColor(chooser.getColor());
                lastColor = chooser.getColor();
                logger.debug("lastColor=" + lastColor);
            }
        });
    }

    public void initComponent()
    {
        if (lastColor == null)
        {
            int code = new Random(System.currentTimeMillis()).nextInt(0xffffff);
            lastColor = new Color(code, false);
        }

        detailPanel.initComponent();
        setColor(lastColor);
    }

    public void readExternal(Element element) throws InvalidDataException
    {
        logger.debug("readExternal");
        detailPanel.readExternal(element);

        Element elem = element.getChild("lastColor");
        if (elem != null)
        {
            String lc = elem.getValue();
            if (lc != null && lc.length() > 0)
            {
                int col = Integer.parseInt(lc);
                lastColor = new Color(col);
            }
        }
    }

    public void writeExternal(Element element) throws WriteExternalException
    {
        logger.debug("writeExternal");
        detailPanel.writeExternal(element);

        logger.debug("lastColor=" + lastColor);
        if (lastColor != null)
        {
            Element lc = new Element("lastColor");
            lc.addContent(Integer.toString(lastColor.getRGB()));
            element.addContent(lc);
        }
        logger.debug("writeExternal-done");
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     */
    private void $$$setupUI$$$()
    {
        pnlMain = new JPanel();
        pnlMain.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnlDetail = new JPanel();
        pnlMain.add(pnlDetail, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        pnlChooser = new JPanel();
        pnlMain.add(pnlChooser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        final JPanel panel1 = new JPanel();
        pnlMain.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
    }

    private JPanel pnlMain;
    private JPanel pnlDetail;

    private Project project;
    private JPanel pnlChooser;

    private DetailPanel detailPanel;
    private JColorChooser chooser;
    private Color lastColor;

    private static Logger logger = Logger.getInstance(ColorBrowserToolWindow.class.getName());
}
