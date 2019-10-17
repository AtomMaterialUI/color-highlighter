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

import com.mallowigi.utils.ColorUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageConsumer;

/**
 * Implements a HSL Color chooser. This class is based on the source code for
 * javax.swing.colorchooser.DefaultHSBChooserPanel.
 */
public class HSLColorChooserPanel extends AbstractColorChooserPanel implements ChangeListener, HierarchyListener {
  private static final int PALETTE_DIMENSION = 200;
  private static final int MAX_HUE_VALUE = 359;
  private static final int MAX_SATURATION_VALUE = 100;
  private static final int MAX_LIGHTNESS_VALUE = 100;
  private static final int HUE_MODE = 0;
  private static final int SATURATION_MODE = 1;
  private static final int LIGHTNESS_MODE = 2;
  private static final long serialVersionUID = -5406221765828843904L;
  private final Point paletteSelection = new Point();
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
  private JLabel paletteLabel;
  private JLabel sliderPaletteLabel;
  private JRadioButton hRadio;
  private JRadioButton sRadio;
  private JRadioButton lRadio;
  private int currentMode = HUE_MODE;

  public HSLColorChooserPanel() {
  }

  /**
   * Invoked automatically when the model's state changes. It is also called by <code>installChooserPanel</code> to
   * allow you to set up the initial state of your chooser. Override this method to update your
   * <code>ChooserPanel</code>.
   */
  @Override
  public void updateChooser() {
    if (!isAdjusting) {
      final float[] hsl = getHSLColorFromModel();
        updateHSL(hsl[0], hsl[1], hsl[2]);
    }
  }

  /**
   * Builds a new chooser panel.
   */
  @Override
  protected void buildChooser() {
      setLayout(new BorderLayout());
    final JComponent spp = buildSliderPalettePanel();
      add(spp, BorderLayout.BEFORE_LINE_BEGINS);

    final JPanel controlHolder = new JPanel(new SmartGridLayout(1, 3));
    final JComponent hslControls = buildHSLControls();
    controlHolder.add(hslControls);

    controlHolder.add(new JLabel(" ")); // spacer

    final JComponent rgbControls = buildRGBControls();
    controlHolder.add(rgbControls);

    controlHolder.setBorder(new EmptyBorder(10, 5, 10, 5));
      add(controlHolder, BorderLayout.CENTER);
  }

  @Override
  public String getDisplayName() {
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
  @Override
  public int getMnemonic() {
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
  @Override
  public int getDisplayedMnemonicIndex() {
    //return getInt("ColorChooser.hsbDisplayedMnemonicIndex", -1);
    return -1;
  }

  @Override
  public Icon getSmallDisplayIcon() {
    return null;
  }

  @Override
  public Icon getLargeDisplayIcon() {
    return null;
  }

  @Override
  public void installChooserPanel(final JColorChooser enclosingChooser) {
    super.installChooserPanel(enclosingChooser);
      addHierarchyListener(this);
  }

  /**
   * Invoked when the panel is removed from the chooser.
   */
  @Override
  public void uninstallChooserPanel(final JColorChooser enclosingChooser) {
    super.uninstallChooserPanel(enclosingChooser);
      cleanupPalettesIfNecessary();
      removeAll();
      removeHierarchyListener(this);
  }

  @Override
  public void stateChanged(final ChangeEvent e) {
    if (e.getSource() == slider) {
      final boolean modelIsAdjusting = slider.getModel().getValueIsAdjusting();

      if (!modelIsAdjusting && !isAdjusting) {
        final int sliderValue = slider.getValue();
        final int sliderRange = slider.getMaximum();
        final float value = (float) sliderValue / (float) sliderRange;

        final float[] hsl = getHSLColorFromModel();

        switch (currentMode) {
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
    } else if (e.getSource() instanceof JSpinner) {
      final float hue = ((Integer) hField.getValue()).floatValue() / 359f;
      final float saturation = ((Integer) sField.getValue()).floatValue() / 100f;
      final float lrightness = ((Integer) lField.getValue()).floatValue() / 100f;

        updateHSL(hue, saturation, lrightness);
    }
  }

  @Override
  public void hierarchyChanged(final HierarchyEvent he) {
    if ((he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
      if (isDisplayable()) {
          initializePalettesIfNecessary();
      } else {
          cleanupPalettesIfNecessary();
      }
    }
  }

  private JComponent buildSliderPalettePanel() {

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

    final JPanel panel = new JPanel();
    panel.add(paletteLabel);
    panel.add(slider);
    panel.add(sliderPaletteLabel);

      initializePalettesIfNecessary();

    return panel;
  }

  private JLabel createPaletteLabel() {
    return new JLabel() {
      private static final long serialVersionUID = -7567900230512109676L;

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.drawOval(paletteSelection.x - 4, paletteSelection.y - 4, 8, 8);
      }
    };
  }

  private void addPaletteListeners() {
      paletteLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(final MouseEvent e) {
        final float[] hsl = new float[3];
          palette.getHSLForLocation(e.getX(), e.getY(), hsl);
          updateHSL(hsl[0], hsl[1], hsl[2]);
      }
    });

      paletteLabel.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(final MouseEvent e) {
        final int labelWidth = paletteLabel.getWidth();

        final int labelHeight = paletteLabel.getHeight();
        int x = e.getX();
        int y = e.getY();

        if (x >= labelWidth) {
          x = labelWidth - 1;
        }

        if (y >= labelHeight) {
          y = labelHeight - 1;
        }

        if (x < 0) {
          x = 0;
        }

        if (y < 0) {
          y = 0;
        }

        final float[] hsl = new float[3];
          palette.getHSLForLocation(x, y, hsl);
          updateHSL(hsl[0], hsl[1], hsl[2]);
      }
    });
  }

  private void updatePalette(final float h, final float s, final float l) {
    int x = 0;
    int y = 0;

    switch (currentMode) {
      case HUE_MODE:
        if (h != palette.getHue()) {
            palette.setHue(h);
            palette.nextFrame();
        }
        x = PALETTE_DIMENSION - (int) (s * PALETTE_DIMENSION);
        y = PALETTE_DIMENSION - (int) (l * PALETTE_DIMENSION);
        break;
      case SATURATION_MODE:
        if (s != palette.getSaturation()) {
            palette.setSaturation(s);
            palette.nextFrame();
        }
        x = (int) (h * PALETTE_DIMENSION);
        y = PALETTE_DIMENSION - (int) (l * PALETTE_DIMENSION);
        break;
      case LIGHTNESS_MODE:
        if (l != palette.getLightness()) {
            palette.setLightness(l);
            palette.nextFrame();
        }
        x = (int) (h * PALETTE_DIMENSION);
        y = PALETTE_DIMENSION - (int) (s * PALETTE_DIMENSION);
        break;
    }

      paletteSelection.setLocation(x, y);
      paletteLabel.repaint();
  }

  private void updateSlider(final float h, final float s, final float l) {
    // Update the slider palette if necessary.
    // When the slider is the hue slider or the hue hasn't changed,
    // the hue of the palette will not need to be updated.
    if (currentMode != HUE_MODE && h != sliderPalette.getHue()) {
        sliderPalette.setHue(h);
        sliderPalette.nextFrame();
    }

    float value = 0f;

    switch (currentMode) {
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

  private void updateHSLTextFields(final float hue, final float saturation, final float lightness) {
    final int h = Math.round(hue * 359);
    final int s = Math.round(saturation * 100);
    final int l = Math.round(lightness * 100);

    if ((Integer) hField.getValue() != h) {
        hField.setValue(h);
    }
    if ((Integer) sField.getValue() != s) {
        sField.setValue(s);
    }
    if ((Integer) lField.getValue() != l) {
        lField.setValue(l);
    }
  }

  /**
   * Updates the values of the RGB fields to reflect the new color change
   */
  private void updateRGBTextFields(final Color color) {
      redField.setText(String.valueOf(color.getRed()));
      greenField.setText(String.valueOf(color.getGreen()));
      blueField.setText(String.valueOf(color.getBlue()));
  }

  /**
   * Main internal method of updating the ui controls and the color model.
   */
  private void updateHSL(final float h, final float s, final float l) {
    if (!isAdjusting) {
        isAdjusting = true;

        updatePalette(h, s, l);
        updateSlider(h, s, l);
        updateHSLTextFields(h, s, l);

      final Color color = ColorUtils.getHSLColor(h, s, l);
        updateRGBTextFields(color);

        getColorSelectionModel().setSelectedColor(color);

        isAdjusting = false;
    }
  }

  /**
   * Returns an float array containing the HSL values of the selected color from the ColorSelectionModel
   */
  private float[] getHSLColorFromModel() {
    final Color color = getColorFromModel();
    final float[] hsl = new float[3];
    ColorUtils.RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), hsl);

    return hsl;
  }

  /**
   * Creates the panel with the uneditable RGB field
   */
  private JComponent buildRGBControls() {
    final JPanel panel = new JPanel(new SmartGridLayout(2, 3));

    final Color color = getColorFromModel();
      redField = new JTextField(String.valueOf(color.getRed()), 3);
      redField.setEditable(false);
      redField.setHorizontalAlignment(JTextField.RIGHT);

      greenField = new JTextField(String.valueOf(color.getGreen()), 3);
      greenField.setEditable(false);
      greenField.setHorizontalAlignment(JTextField.RIGHT);

      blueField = new JTextField(String.valueOf(color.getBlue()), 3);
      blueField.setEditable(false);
      blueField.setHorizontalAlignment(JTextField.RIGHT);

    final String redString = UIManager.getString("ColorChooser.hsbRedText");
    final String greenString = UIManager.getString("ColorChooser.hsbGreenText");
    final String blueString = UIManager.getString("ColorChooser.hsbBlueText");

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
  private JComponent buildHSLControls() {

    final String hueString = UIManager.getString("ColorChooser.hsbHueText");
    final String saturationString = UIManager.getString("ColorChooser.hsbSaturationText");
    final String lightnessString = "L"; //UIManager.getString("ColorChooser.hsbBrightnessText");

    final RadioButtonHandler handler = new RadioButtonHandler();

      hRadio = new JRadioButton(hueString);
      hRadio.addActionListener(handler);

      sRadio = new JRadioButton(saturationString);
      sRadio.addActionListener(handler);

      lRadio = new JRadioButton(lightnessString);
      lRadio.addActionListener(handler);

    final ButtonGroup group = new ButtonGroup();
    group.add(hRadio);
    group.add(sRadio);
    group.add(lRadio);

    final float[] hsl = getHSLColorFromModel();

      hField = new JSpinner(new SpinnerNumberModel((int) (hsl[0] * 359), 0, 359, 1));
      sField = new JSpinner(new SpinnerNumberModel((int) (hsl[1] * 100), 0, 100, 1));
      lField = new JSpinner(new SpinnerNumberModel((int) (hsl[2] * 100), 0, 100, 1));

      hField.addChangeListener(this);
      sField.addChangeListener(this);
      lField.addChangeListener(this);

      hRadio.setSelected(true);
      sRadio.setSelected(true);
      hRadio.setSelected(true);

    final JPanel panel = new JPanel(new SmartGridLayout(2, 3));

    panel.add(hRadio);
    panel.add(hField);
    panel.add(sRadio);
    panel.add(sField);
    panel.add(lRadio);
    panel.add(lField);

    return panel;
  }

  private void setMode(final int mode) {
    if (currentMode == mode) {
      return;
    }

      isAdjusting = true;  // Ensure no events propagate from changing slider value.
      currentMode = mode;

    final float[] hsl = getHSLColorFromModel();

    switch (currentMode) {
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

  private void initializePalettesIfNecessary() {
    if (palette != null) {
      return;
    }

    final float[] hsl = getHSLColorFromModel();

    switch (currentMode) {
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
    final Image paletteImage = Toolkit.getDefaultToolkit().createImage(palette);
    final Image sliderPaletteImage = Toolkit.getDefaultToolkit().createImage(sliderPalette);

      paletteLabel.setIcon(new ImageIcon(paletteImage));
      sliderPaletteLabel.setIcon(new ImageIcon(sliderPaletteImage));
  }

  private void cleanupPalettesIfNecessary() {
    if (palette == null) {
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

  /**
   * Handler for the radio button classes.
   */
  private class RadioButtonHandler implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent evt) {
      final Object obj = evt.getSource();

      if (obj instanceof JRadioButton) {
        final JRadioButton button = (JRadioButton) obj;
        if (button == hRadio) {
            setMode(HUE_MODE);
        } else if (button == sRadio) {
            setMode(SATURATION_MODE);
        } else if (button == lRadio) {
            setMode(LIGHTNESS_MODE);
        }
      }
    }
  }

  /**
   * Class for the slider and palette images.
   */
  class HSLImage extends SyntheticImage {
    private static final int HSQUARE = 0;
    private static final int SSQUARE = 1;
    private static final int BSQUARE = 2;
    private static final int HSLIDER = 3;
    private static final int SSLIDER = 4;
    private static final int BSLIDER = 5;
    float h = .0f;
    float s = .0f;
    float l = .0f;
    float[] hsl = new float[3];
    boolean isDirty = true;
    int cachedY;
    int cachedColor;
    int type;

    HSLImage(final int type, final int width, final int height, final float h, final float s, final float l) {
      super(width, height);
        setValues(type, h, s, l);
    }

    @Override
    public synchronized void addConsumer(final ImageConsumer ic) {
        isDirty = true;
      super.addConsumer(ic);
    }

    /**
     * Overriden method from SyntheticImage
     */
    @Override
    protected void computeRow(final int y, final int[] row) {
      if (y == 0) {
        synchronized (this) {
          try {
            while (!isDirty) {
                wait();
            }
          } catch (final InterruptedException ie) {
            // no-op
          }
            isDirty = false;
        }
      }

      if (aborted) {
        return;
      }

      for (int i = 0; i < row.length; ++i) {
        row[i] = getRGBForLocation(i, y);
      }
    }

    @Override
    protected boolean isStatic() {
      return false;
    }

    void setValues(final int type, final float h, final float s, final float l) {
      this.type = type;
        cachedY = -1;
        cachedColor = 0;
        setHue(h);
        setSaturation(s);
        setLightness(l);
    }

    final float getHue() {
      return h;
    }

    final void setHue(final float hue) {
        h = hue;
    }

    final float getSaturation() {
      return s;
    }

    final void setSaturation(final float saturation) {
        s = saturation;
    }

    final float getLightness() {
      return l;
    }

    final void setLightness(final float lightness) {
        l = lightness;
    }

    synchronized void nextFrame() {
        isDirty = true;
        notifyAll();
    }

    void getHSLForLocation(final int x, final int y, final float[] hslArray) {
      switch (type) {
        case HSQUARE: {
          final float saturationStep = ((float) x) / width;
          final float lightnessStep = ((float) y) / height;
          hslArray[0] = h;
          hslArray[1] = s - saturationStep;
          hslArray[2] = l - lightnessStep;
          break;
        }
        case SSQUARE: {
          final float lightnessStep = ((float) y) / height;
          final float step = 1.0f / ((float) width);
          hslArray[0] = x * step;
          hslArray[1] = s;
          hslArray[2] = 1.0f - lightnessStep;
          break;
        }
        case BSQUARE: {
          final float saturationStep = ((float) y) / height;
          final float step = 1.0f / ((float) width);
          hslArray[0] = x * step;
          hslArray[1] = 1.0f - saturationStep;
          hslArray[2] = l;
          break;
        }
        case HSLIDER: {
          final float step = 1.0f / ((float) height);
          hslArray[0] = y * step;
          hslArray[1] = s;
          hslArray[2] = l;
          break;
        }
        case SSLIDER: {
          final float saturationStep = ((float) y) / height;
          hslArray[0] = h;
          hslArray[1] = s - saturationStep;
          hslArray[2] = l;
          break;
        }
        case BSLIDER: {
          final float lightnessStep = ((float) y) / height;
          hslArray[0] = h;
          hslArray[1] = s;
          hslArray[2] = l - lightnessStep;
          break;
        }
      }
    }

    private int getRGBForLocation(final int x, final int y) {
      if (type >= HSLIDER && y == cachedY) {
        return cachedColor;
      }

        getHSLForLocation(x, y, hsl);
        cachedY = y;
        cachedColor = ColorUtils.HSLtoRGB(hsl[0], hsl[1], hsl[2]);

      return cachedColor;
    }
  }
}
