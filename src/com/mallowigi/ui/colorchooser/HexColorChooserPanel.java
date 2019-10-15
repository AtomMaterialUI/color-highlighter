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

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.ParseException;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 * Implements a hex based RGB Color chooser. This class is based on the source code for
 * javax.swing.colorchooser.DefaultRGBChooserPanel.
 */
public class HexColorChooserPanel extends AbstractColorChooserPanel implements ChangeListener
{
    protected JSlider redSlider;
    protected JSlider greenSlider;
    protected JSlider blueSlider;
    protected JSpinner redField;
    protected JSpinner blueField;
    protected JSpinner greenField;

    private final int minValue = 0;
    private final int maxValue = 255;

    private boolean isAdjusting = false; // indicates the fields are being set internally

    public HexColorChooserPanel()
    {
        super();
    }

    /**
     * Sets the values of the controls to reflect the color
     */
    private void setColor(Color newColor)
    {
        int red = newColor.getRed();
        int blue = newColor.getBlue();
        int green = newColor.getGreen();

        if (redSlider.getValue() != red)
        {
            redSlider.setValue(red);
        }
        if (greenSlider.getValue() != green)
        {
            greenSlider.setValue(green);
        }
        if (blueSlider.getValue() != blue)
        {
            blueSlider.setValue(blue);
        }

        if ((Integer)redField.getValue() != red)
        {
            redField.setValue(red);
        }
        if ((Integer)greenField.getValue() != green)
        {
            greenField.setValue(green);
        }
        if ((Integer)blueField.getValue() != blue)
        {
            blueField.setValue(blue);
        }
    }

    public String getDisplayName()
    {
        //return UIManager.getString("ColorChooser.rgbNameText");
        return "Hex";
    }

    /**
     * Provides a hint to the look and feel as to the <code>KeyEvent.VK</code> constant that can be used as a mnemonic
     * to access the panel. A return value <= 0 indicates there is no mnemonic.
     * <p/>
     * The return value here is a hint, it is ultimately up to the look and feel to honor the return value in some
     * meaningful way.
     * <p/>
     * This implementation looks up the value from the default <code>ColorChooser.rgbMnemonic</code>, or if it isn't
     * available (or not an <code>Integer</code>) returns -1. The lookup for the default is done through the
     * <code>UIManager</code>: <code>UIManager.get("ColorChooser.rgbMnemonic");</code>.
     *
     * @return KeyEvent.VK constant identifying the mnemonic; <= 0 for no mnemonic
     * @see #getDisplayedMnemonicIndex
     * @since 1.4
     */
    public int getMnemonic()
    {
        //return getInt("ColorChooser.rgbMnemonic", -1);
        return -1;
    }

    /**
     * Provides a hint to the look and feel as to the index of the character in <code>getDisplayName</code> that should
     * be visually identified as the mnemonic. The look and feel should only use this if <code>getMnemonic</code>
     * returns a value > 0.
     * <p/>
     * The return value here is a hint, it is ultimately up to the look and feel to honor the return value in some
     * meaningful way. For example, a look and feel may wish to render each <code>AbstractColorChooserPanel</code> in a
     * <code>JTabbedPane</code>, and further use this return value to underline a character in the
     * <code>getDisplayName</code>.
     * <p/>
     * This implementation looks up the value from the default <code>ColorChooser.rgbDisplayedMnemonicIndex</code>, or
     * if it isn't available (or not an <code>Integer</code>) returns -1. The lookup for the default is done through the
     * <code>UIManager</code>: <code>UIManager.get("ColorChooser.rgbDisplayedMnemonicIndex");</code>.
     *
     * @return Character index to render mnemonic for; -1 to provide no visual identifier for this panel.
     * @see #getMnemonic
     * @since 1.4
     */
    public int getDisplayedMnemonicIndex()
    {
        //return getInt("ColorChooser.rgbDisplayedMnemonicIndex", -1);
        return -1;
    }

    public Icon getSmallDisplayIcon()
    {
        return null;
    }

    public Icon getLargeDisplayIcon()
    {
        return null;
    }

    protected void buildChooser()
    {

        String redString = UIManager.getString("ColorChooser.rgbRedText");
        String greenString = UIManager.getString("ColorChooser.rgbGreenText");
        String blueString = UIManager.getString("ColorChooser.rgbBlueText");

        setLayout(new BorderLayout());
        Color color = getColorFromModel();

        JPanel enclosure = new JPanel();
        enclosure.setLayout(new SmartGridLayout(3, 3));

        // The panel that holds the sliders

        add(enclosure, BorderLayout.CENTER);
        //        sliderPanel.setBorder(new LineBorder(Color.black));

        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        for (int l = 0; l <= 255; l+=64)
        {
            labels.put(l, new JLabel(String.format("%02X", l)));
        }
        labels.put(255, new JLabel(String.format("%02X", 255)));

        // The row for the red value
        JLabel l = new JLabel(redString);
        //l.setDisplayedMnemonic(AbstractColorChooserPanel.getInt("ColorChooser.rgbRedMnemonic", -1));
        enclosure.add(l);
        redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getRed());
        redSlider.setMajorTickSpacing(64);
        redSlider.setMinorTickSpacing(16);
        redSlider.setPaintTicks(true);
        redSlider.setPaintLabels(true);
        redSlider.setLabelTable(labels);
        enclosure.add(redSlider);
        redField = new JSpinner(
            new SpinnerNumberModel(color.getRed(), minValue, maxValue, 1));
        ((JSpinner.DefaultEditor)redField.getEditor()).getTextField().setFormatterFactory(new HexFormatterFactory());
        l.setLabelFor(redSlider);
        JPanel redFieldHolder = new JPanel(new CenterLayout());
        redField.addChangeListener(this);
        redFieldHolder.add(redField);
        enclosure.add(redFieldHolder);

        // The row for the green value
        l = new JLabel(greenString);
        //l.setDisplayedMnemonic(AbstractColorChooserPanel.getInt("ColorChooser.rgbGreenMnemonic", -1));
        enclosure.add(l);
        greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getGreen());
        greenSlider.setMajorTickSpacing(64);
        greenSlider.setMinorTickSpacing(16);
        greenSlider.setPaintTicks(true);
        greenSlider.setPaintLabels(true);
        greenSlider.setLabelTable(labels);
        enclosure.add(greenSlider);
        greenField = new JSpinner(
            new SpinnerNumberModel(color.getGreen(), minValue, maxValue, 1));
        ((JSpinner.DefaultEditor)greenField.getEditor()).getTextField().setFormatterFactory(new HexFormatterFactory());
        l.setLabelFor(greenSlider);
        JPanel greenFieldHolder = new JPanel(new CenterLayout());
        greenFieldHolder.add(greenField);
        greenField.addChangeListener(this);
        enclosure.add(greenFieldHolder);

        // The slider for the blue value
        l = new JLabel(blueString);
        //l.setDisplayedMnemonic(AbstractColorChooserPanel.getInt("ColorChooser.rgbBlueMnemonic", -1));
        enclosure.add(l);
        blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getBlue());
        blueSlider.setMajorTickSpacing(64);
        blueSlider.setMinorTickSpacing(16);
        blueSlider.setPaintTicks(true);
        blueSlider.setPaintLabels(true);
        blueSlider.setLabelTable(labels);
        enclosure.add(blueSlider);
        blueField = new JSpinner(
            new SpinnerNumberModel(color.getBlue(), minValue, maxValue, 1));
        ((JSpinner.DefaultEditor)blueField.getEditor()).getTextField().setFormatterFactory(new HexFormatterFactory());
        l.setLabelFor(blueSlider);
        JPanel blueFieldHolder = new JPanel(new CenterLayout());
        blueFieldHolder.add(blueField);
        blueField.addChangeListener(this);
        enclosure.add(blueFieldHolder);

        redSlider.addChangeListener(this);
        greenSlider.addChangeListener(this);
        blueSlider.addChangeListener(this);

        redSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        greenSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        blueSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
    }

    public void uninstallChooserPanel(JColorChooser enclosingChooser)
    {
        super.uninstallChooserPanel(enclosingChooser);
        removeAll();
    }

    public void updateChooser()
    {
        if (!isAdjusting)
        {
            isAdjusting = true;

            setColor(getColorFromModel());

            isAdjusting = false;
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() instanceof JSlider && !isAdjusting)
        {

            int red = redSlider.getValue();
            int green = greenSlider.getValue();
            int blue = blueSlider.getValue();
            Color color = new Color(red, green, blue);

            getColorSelectionModel().setSelectedColor(color);
        }
        else if (e.getSource() instanceof JSpinner && !isAdjusting)
        {

            int red = (Integer)redField.getValue();
            int green = (Integer)greenField.getValue();
            int blue = (Integer)blueField.getValue();
            Color color = new Color(red, green, blue);

            getColorSelectionModel().setSelectedColor(color);
        }
    }

    private static class HexFormatterFactory extends DefaultFormatterFactory
    {
        public JFormattedTextField.AbstractFormatter getDefaultFormatter()
        {
            return new HexFormatter();
        }
    }

    private static class HexFormatter extends DefaultFormatter
    {
        public Object stringToValue(String string) throws ParseException
        {
            try
            {
                return Integer.parseInt(string, 16);
            }
            catch (NumberFormatException e)
            {
                throw new ParseException(string, 0);
            }
        }

        public String valueToString(Object object) throws ParseException
        {
            return String.format("%02X", (Integer)object);
        }
    }
}


