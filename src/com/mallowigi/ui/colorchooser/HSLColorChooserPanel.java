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

import com.maddyhome.idea.color.util.ColorUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.ImageConsumer;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Implements a HSL Color chooser. This class is based on the source code for
 * javax.swing.colorchooser.DefaultHSBChooserPanel.
 */
public class HSLColorChooserPanel extends AbstractColorChooserPanel implements ChangeListener, HierarchyListener
{
    private transient HSLImage palette;
    private transient HSLImage sliderPalette;

    private JSlider slider;
    private JSpinner hField;
    private JSpinner sField;
    private JSpinner lField;

    private JTextField redField;
    private JTextField greenField;
    private JTextField blueField;

    private boolean isAdjusting = false; // Flag which indicates that values are set internally
    private Point paletteSelection = new Point();
    private JLabel paletteLabel;
    private JLabel sliderPaletteLabel;

    private JRadioButton hRadio;
    private JRadioButton sRadio;
    private JRadioButton lRadio;

    private static final int PALETTE_DIMENSION = 200;
    private static final int MAX_HUE_VALUE = 359;
    private static final int MAX_SATURATION_VALUE = 100;
    private static final int MAX_LIGHTNESS_VALUE = 100;

    private int currentMode = HUE_MODE;

    private static final int HUE_MODE = 0;
    private static final int SATURATION_MODE = 1;
    private static final int LIGHTNESS_MODE = 2;

    public HSLColorChooserPanel()
    {
    }

    private void addPaletteListeners()
    {
        paletteLabel.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                float[] hsl = new float[3];
                palette.getHSLForLocation(e.getX(), e.getY(), hsl);
                updateHSL(hsl[0], hsl[1], hsl[2]);
            }
        });

        paletteLabel.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int labelWidth = paletteLabel.getWidth();

                int labelHeight = paletteLabel.getHeight();
                int x = e.getX();
                int y = e.getY();

                if (x >= labelWidth)
                {
                    x = labelWidth - 1;
                }

                if (y >= labelHeight)
                {
                    y = labelHeight - 1;
                }

                if (x < 0)
                {
                    x = 0;
                }

                if (y < 0)
                {
                    y = 0;
                }

                float[] hsl = new float[3];
                palette.getHSLForLocation(x, y, hsl);
                updateHSL(hsl[0], hsl[1], hsl[2]);
            }
        });
    }

    private void updatePalette(float h, float s, float l)
    {
        int x = 0;
        int y = 0;

        switch (currentMode)
        {
            case HUE_MODE:
                if (h != palette.getHue())
                {
                    palette.setHue(h);
                    palette.nextFrame();
                }
                x = PALETTE_DIMENSION - (int)(s * PALETTE_DIMENSION);
                y = PALETTE_DIMENSION - (int)(l * PALETTE_DIMENSION);
                break;
            case SATURATION_MODE:
                if (s != palette.getSaturation())
                {
                    palette.setSaturation(s);
                    palette.nextFrame();
                }
                x = (int)(h * PALETTE_DIMENSION);
                y = PALETTE_DIMENSION - (int)(l * PALETTE_DIMENSION);
                break;
            case LIGHTNESS_MODE:
                if (l != palette.getLightness())
                {
                    palette.setLightness(l);
                    palette.nextFrame();
                }
                x = (int)(h * PALETTE_DIMENSION);
                y = PALETTE_DIMENSION - (int)(s * PALETTE_DIMENSION);
                break;
        }

        paletteSelection.setLocation(x, y);
        paletteLabel.repaint();
    }

    private void updateSlider(float h, float s, float l)
    {
        // Update the slider palette if necessary.
        // When the slider is the hue slider or the hue hasn't changed,
        // the hue of the palette will not need to be updated.
        if (currentMode != HUE_MODE && h != sliderPalette.getHue())
        {
            sliderPalette.setHue(h);
            sliderPalette.nextFrame();
        }

        float value = 0f;

        switch (currentMode)
        {
            case HUE_MODE:
                value = h;
                break;
            case SATURATION_MODE:
                value = s;
                break;
            case LIGHTNESS_MODE:
                value = l;
                break;
        }

        slider.setValue(Math.round(value * (slider.getMaximum())));
    }

    private void updateHSLTextFields(float hue, float saturation, float lightness)
    {
        int h = Math.round(hue * 359);
        int s = Math.round(saturation * 100);
        int l = Math.round(lightness * 100);

        if ((Integer)hField.getValue() != h)
        {
            hField.setValue(h);
        }
        if ((Integer)sField.getValue() != s)
        {
            sField.setValue(s);
        }
        if ((Integer)lField.getValue() != l)
        {
            lField.setValue(l);
        }
    }

    /**
     * Updates the values of the RGB fields to reflect the new color change
     */
    private void updateRGBTextFields(Color color)
    {
        redField.setText(String.valueOf(color.getRed()));
        greenField.setText(String.valueOf(color.getGreen()));
        blueField.setText(String.valueOf(color.getBlue()));
    }

    /**
     * Main internal method of updating the ui controls and the color model.
     */
    private void updateHSL(float h, float s, float l)
    {
        if (!isAdjusting)
        {
            isAdjusting = true;

            updatePalette(h, s, l);
            updateSlider(h, s, l);
            updateHSLTextFields(h, s, l);

            Color color = ColorUtil.getHSLColor(h, s, l);
            updateRGBTextFields(color);

            getColorSelectionModel().setSelectedColor(color);

            isAdjusting = false;
        }
    }

    /**
     * Invoked automatically when the model's state changes. It is also called by <code>installChooserPanel</code> to
     * allow you to set up the initial state of your chooser. Override this method to update your
     * <code>ChooserPanel</code>.
     */
    public void updateChooser()
    {
        if (!isAdjusting)
        {
            float[] hsl = getHSLColorFromModel();
            updateHSL(hsl[0], hsl[1], hsl[2]);
        }
    }

    public void installChooserPanel(JColorChooser enclosingChooser)
    {
        super.installChooserPanel(enclosingChooser);
        addHierarchyListener(this);
    }

    /**
     * Invoked when the panel is removed from the chooser.
     */
    public void uninstallChooserPanel(JColorChooser enclosingChooser)
    {
        super.uninstallChooserPanel(enclosingChooser);
        cleanupPalettesIfNecessary();
        removeAll();
        removeHierarchyListener(this);
    }

    /**
     * Returns an float array containing the HSL values of the selected color from the ColorSelectionModel
     */
    private float[] getHSLColorFromModel()
    {
        Color color = getColorFromModel();
        float[] hsl = new float[3];
        ColorUtil.RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), hsl);

        return hsl;
    }

    /**
     * Builds a new chooser panel.
     */
    protected void buildChooser()
    {
        setLayout(new BorderLayout());
        JComponent spp = buildSliderPalettePanel();
        add(spp, BorderLayout.BEFORE_LINE_BEGINS);

        JPanel controlHolder = new JPanel(new SmartGridLayout(1, 3));
        JComponent hslControls = buildHSLControls();
        controlHolder.add(hslControls);

        controlHolder.add(new JLabel(" ")); // spacer

        JComponent rgbControls = buildRGBControls();
        controlHolder.add(rgbControls);

        controlHolder.setBorder(new EmptyBorder(10, 5, 10, 5));
        add(controlHolder, BorderLayout.CENTER);
    }

    /**
     * Creates the panel with the uneditable RGB field
     */
    private JComponent buildRGBControls()
    {
        JPanel panel = new JPanel(new SmartGridLayout(2, 3));

        Color color = getColorFromModel();
        redField = new JTextField(String.valueOf(color.getRed()), 3);
        redField.setEditable(false);
        redField.setHorizontalAlignment(JTextField.RIGHT);

        greenField = new JTextField(String.valueOf(color.getGreen()), 3);
        greenField.setEditable(false);
        greenField.setHorizontalAlignment(JTextField.RIGHT);

        blueField = new JTextField(String.valueOf(color.getBlue()), 3);
        blueField.setEditable(false);
        blueField.setHorizontalAlignment(JTextField.RIGHT);

        String redString = UIManager.getString("ColorChooser.hsbRedText");
        String greenString = UIManager.getString("ColorChooser.hsbGreenText");
        String blueString = UIManager.getString("ColorChooser.hsbBlueText");

        panel.add(new JLabel(redString));
        panel.add(redField);
        panel.add(new JLabel(greenString));
        panel.add(greenField);
        panel.add(new JLabel(blueString));
        panel.add(blueField);

        return panel;
    }

    /**
     * Creates the panel with the editable HSL fields and the radio buttons.
     */
    private JComponent buildHSLControls()
    {

        String hueString = UIManager.getString("ColorChooser.hsbHueText");
        String saturationString = UIManager.getString("ColorChooser.hsbSaturationText");
        String lightnessString = "L"; //UIManager.getString("ColorChooser.hsbBrightnessText");

        RadioButtonHandler handler = new RadioButtonHandler();

        hRadio = new JRadioButton(hueString);
        hRadio.addActionListener(handler);

        sRadio = new JRadioButton(saturationString);
        sRadio.addActionListener(handler);

        lRadio = new JRadioButton(lightnessString);
        lRadio.addActionListener(handler);

        ButtonGroup group = new ButtonGroup();
        group.add(hRadio);
        group.add(sRadio);
        group.add(lRadio);

        float[] hsl = getHSLColorFromModel();

        hField = new JSpinner(new SpinnerNumberModel((int)(hsl[0] * 359), 0, 359, 1));
        sField = new JSpinner(new SpinnerNumberModel((int)(hsl[1] * 100), 0, 100, 1));
        lField = new JSpinner(new SpinnerNumberModel((int)(hsl[2] * 100), 0, 100, 1));

        hField.addChangeListener(this);
        sField.addChangeListener(this);
        lField.addChangeListener(this);

        hRadio.setSelected(true);
        sRadio.setSelected(true);
        hRadio.setSelected(true);

        JPanel panel = new JPanel(new SmartGridLayout(2, 3));

        panel.add(hRadio);
        panel.add(hField);
        panel.add(sRadio);
        panel.add(sField);
        panel.add(lRadio);
        panel.add(lField);

        return panel;
    }

    /**
     * Handler for the radio button classes.
     */
    private class RadioButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            Object obj = evt.getSource();

            if (obj instanceof JRadioButton)
            {
                JRadioButton button = (JRadioButton)obj;
                if (button == hRadio)
                {
                    setMode(HUE_MODE);
                }
                else if (button == sRadio)
                {
                    setMode(SATURATION_MODE);
                }
                else if (button == lRadio)
                {
                    setMode(LIGHTNESS_MODE);
                }
            }
        }
    }

    private void setMode(int mode)
    {
        if (currentMode == mode)
        {
            return;
        }

        isAdjusting = true;  // Ensure no events propagate from changing slider value.
        currentMode = mode;

        float[] hsl = getHSLColorFromModel();

        switch (currentMode)
        {
            case HUE_MODE:
                slider.setInverted(true);
                slider.setMaximum(MAX_HUE_VALUE);
                palette.setValues(HSLImage.HSQUARE, hsl[0], 1.0f, 1.0f);
                sliderPalette.setValues(HSLImage.HSLIDER, 0f, 1.0f, 0.5f);
                break;
            case SATURATION_MODE:
                slider.setInverted(false);
                slider.setMaximum(MAX_SATURATION_VALUE);
                palette.setValues(HSLImage.SSQUARE, hsl[0], hsl[1], 1.0f);
                sliderPalette.setValues(HSLImage.SSLIDER, hsl[0], 1.0f, 0.5f);
                break;
            case LIGHTNESS_MODE:
                slider.setInverted(false);
                slider.setMaximum(MAX_LIGHTNESS_VALUE);
                palette.setValues(HSLImage.BSQUARE, hsl[0], 1.0f, hsl[2]);
                sliderPalette.setValues(HSLImage.BSLIDER, hsl[0], 1.0f, 1.0f);
                break;
        }

        isAdjusting = false;

        palette.nextFrame();
        sliderPalette.nextFrame();

        updateChooser();
    }

    protected JComponent buildSliderPalettePanel()
    {

        // This slider has to have a minimum of 0.  A lot of math in this file is simplified due to this.
        slider = new JSlider(JSlider.VERTICAL, 0, MAX_HUE_VALUE, 0);
        slider.setInverted(true);
        slider.setPaintTrack(false);
        slider.setPreferredSize(new Dimension(slider.getPreferredSize().width, PALETTE_DIMENSION + 15));
        slider.addChangeListener(this);
        // We're not painting ticks, but need to ask UI classes to
        // paint arrow shape anyway, if possible.
        slider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
        paletteLabel = createPaletteLabel();
        addPaletteListeners();
        sliderPaletteLabel = new JLabel();

        JPanel panel = new JPanel();
        panel.add(paletteLabel);
        panel.add(slider);
        panel.add(sliderPaletteLabel);

        initializePalettesIfNecessary();

        return panel;
    }

    private void initializePalettesIfNecessary()
    {
        if (palette != null)
        {
            return;
        }

        float[] hsl = getHSLColorFromModel();

        switch (currentMode)
        {
            case HUE_MODE:
                palette = new HSLImage(HSLImage.HSQUARE, PALETTE_DIMENSION, PALETTE_DIMENSION, hsl[0], 1.0f, 1.0f);
                sliderPalette = new HSLImage(HSLImage.HSLIDER, 16, PALETTE_DIMENSION, 0f, 1.0f, 0.5f);
                break;
            case SATURATION_MODE:
                palette = new HSLImage(HSLImage.SSQUARE, PALETTE_DIMENSION, PALETTE_DIMENSION, 1.0f, hsl[1], 1.0f);
                sliderPalette = new HSLImage(HSLImage.SSLIDER, 16, PALETTE_DIMENSION, 1.0f, 0f, 0.5f);
                break;
            case LIGHTNESS_MODE:
                palette = new HSLImage(HSLImage.BSQUARE, PALETTE_DIMENSION, PALETTE_DIMENSION, 1.0f, 1.0f, hsl[2]);
                sliderPalette = new HSLImage(HSLImage.BSLIDER, 16, PALETTE_DIMENSION, 1.0f, 1.0f, 0f);
                break;
        }
        Image paletteImage = Toolkit.getDefaultToolkit().createImage(palette);
        Image sliderPaletteImage = Toolkit.getDefaultToolkit().createImage(sliderPalette);

        paletteLabel.setIcon(new ImageIcon(paletteImage));
        sliderPaletteLabel.setIcon(new ImageIcon(sliderPaletteImage));
    }

    private void cleanupPalettesIfNecessary()
    {
        if (palette == null)
        {
            return;
        }

        palette.aborted = true;
        sliderPalette.aborted = true;

        palette.nextFrame();
        sliderPalette.nextFrame();

        palette = null;
        sliderPalette = null;

        paletteLabel.setIcon(null);
        sliderPaletteLabel.setIcon(null);
    }

    protected JLabel createPaletteLabel()
    {
        return new JLabel()
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.white);
                g.drawOval(paletteSelection.x - 4, paletteSelection.y - 4, 8, 8);
            }
        };
    }

    public String getDisplayName()
    {
        //return UIManager.getString("ColorChooser.hsbNameText");
        return "HSL";
    }

    /**
     * Provides a hint to the look and feel as to the <code>KeyEvent.VK</code> constant that can be used as a mnemonic
     * to access the panel. A return value <= 0 indicates there is no mnemonic.
     * <p/>
     * The return value here is a hint, it is ultimately up to the look and feel to honor the return value in some
     * meaningful way.
     * <p/>
     * This implementation looks up the value from the default <code>ColorChooser.hsbMnemonic</code>, or if it isn't
     * available (or not an <code>Integer</code>) returns -1. The lookup for the default is done through the
     * <code>UIManager</code>: <code>UIManager.get("ColorChooser.rgbMnemonic");</code>.
     *
     * @return KeyEvent.VK constant identifying the mnemonic; <= 0 for no mnemonic
     * @see #getDisplayedMnemonicIndex
     * @since 1.4
     */
    public int getMnemonic()
    {
        //return getInt("ColorChooser.hsbMnemonic", -1);
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
     * <code>UIManager</code>: <code>UIManager.get("ColorChooser.hsbDisplayedMnemonicIndex");</code>.
     *
     * @return Character index to render mnemonic for; -1 to provide no visual identifier for this panel.
     * @see #getMnemonic
     * @since 1.4
     */
    public int getDisplayedMnemonicIndex()
    {
        //return getInt("ColorChooser.hsbDisplayedMnemonicIndex", -1);
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

    /**
     * Class for the slider and palette images.
     */
    class HSLImage extends SyntheticImage
    {
        protected float h = .0f;
        protected float s = .0f;
        protected float l = .0f;
        protected float[] hsl = new float[3];

        protected boolean isDirty = true;
        protected int cachedY;
        protected int cachedColor;
        protected int type;

        private static final int HSQUARE = 0;
        private static final int SSQUARE = 1;
        private static final int BSQUARE = 2;
        private static final int HSLIDER = 3;
        private static final int SSLIDER = 4;
        private static final int BSLIDER = 5;

        protected HSLImage(int type, int width, int height, float h, float s, float l)
        {
            super(width, height);
            setValues(type, h, s, l);
        }

        public void setValues(int type, float h, float s, float l)
        {
            this.type = type;
            cachedY = -1;
            cachedColor = 0;
            setHue(h);
            setSaturation(s);
            setLightness(l);
        }

        public final void setHue(float hue)
        {
            h = hue;
        }

        public final void setSaturation(float saturation)
        {
            s = saturation;
        }

        public final void setLightness(float lightness)
        {
            l = lightness;
        }

        public final float getHue()
        {
            return h;
        }

        public final float getSaturation()
        {
            return s;
        }

        public final float getLightness()
        {
            return l;
        }

        protected boolean isStatic()
        {
            return false;
        }

        public synchronized void nextFrame()
        {
            isDirty = true;
            notifyAll();
        }

        public synchronized void addConsumer(ImageConsumer ic)
        {
            isDirty = true;
            super.addConsumer(ic);
        }

        private int getRGBForLocation(int x, int y)
        {
            if (type >= HSLIDER && y == cachedY)
            {
                return cachedColor;
            }

            getHSLForLocation(x, y, hsl);
            cachedY = y;
            cachedColor = ColorUtil.HSLtoRGB(hsl[0], hsl[1], hsl[2]);

            return cachedColor;
        }

        public void getHSLForLocation(int x, int y, float[] hslArray)
        {
            switch (type)
            {
                case HSQUARE:
                {
                    float saturationStep = ((float)x) / width;
                    float lightnessStep = ((float)y) / height;
                    hslArray[0] = h;
                    hslArray[1] = s - saturationStep;
                    hslArray[2] = l - lightnessStep;
                    break;
                }
                case SSQUARE:
                {
                    float lightnessStep = ((float)y) / height;
                    float step = 1.0f / ((float)width);
                    hslArray[0] = x * step;
                    hslArray[1] = s;
                    hslArray[2] = 1.0f - lightnessStep;
                    break;
                }
                case BSQUARE:
                {
                    float saturationStep = ((float)y) / height;
                    float step = 1.0f / ((float)width);
                    hslArray[0] = x * step;
                    hslArray[1] = 1.0f - saturationStep;
                    hslArray[2] = l;
                    break;
                }
                case HSLIDER:
                {
                    float step = 1.0f / ((float)height);
                    hslArray[0] = y * step;
                    hslArray[1] = s;
                    hslArray[2] = l;
                    break;
                }
                case SSLIDER:
                {
                    float saturationStep = ((float)y) / height;
                    hslArray[0] = h;
                    hslArray[1] = s - saturationStep;
                    hslArray[2] = l;
                    break;
                }
                case BSLIDER:
                {
                    float lightnessStep = ((float)y) / height;
                    hslArray[0] = h;
                    hslArray[1] = s;
                    hslArray[2] = l - lightnessStep;
                    break;
                }
            }
        }

        /**
         * Overriden method from SyntheticImage
         */
        protected void computeRow(int y, int[] row)
        {
            if (y == 0)
            {
                synchronized (this)
                {
                    try
                    {
                        while (!isDirty)
                        {
                            wait();
                        }
                    }
                    catch (InterruptedException ie)
                    {
                        // no-op
                    }
                    isDirty = false;
                }
            }

            if (aborted)
            {
                return;
            }

            for (int i = 0; i < row.length; ++i)
            {
                row[i] = getRGBForLocation(i, y);
            }
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == slider)
        {
            boolean modelIsAdjusting = slider.getModel().getValueIsAdjusting();

            if (!modelIsAdjusting && !isAdjusting)
            {
                int sliderValue = slider.getValue();
                int sliderRange = slider.getMaximum();
                float value = (float)sliderValue / (float)sliderRange;

                float[] hsl = getHSLColorFromModel();

                switch (currentMode)
                {
                    case HUE_MODE:
                        updateHSL(value, hsl[1], hsl[2]);
                        break;
                    case SATURATION_MODE:
                        updateHSL(hsl[0], value, hsl[2]);
                        break;
                    case LIGHTNESS_MODE:
                        updateHSL(hsl[0], hsl[1], value);
                        break;
                }
            }
        }
        else if (e.getSource() instanceof JSpinner)
        {
            float hue = ((Integer)hField.getValue()).floatValue() / 359f;
            float saturation = ((Integer)sField.getValue()).floatValue() / 100f;
            float lrightness = ((Integer)lField.getValue()).floatValue() / 100f;

            updateHSL(hue, saturation, lrightness);
        }
    }

    public void hierarchyChanged(HierarchyEvent he)
    {
        if ((he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0)
        {
            if (isDisplayable())
            {
                initializePalettesIfNecessary();
            }
            else
            {
                cleanupPalettesIfNecessary();
            }
        }
    }
}
