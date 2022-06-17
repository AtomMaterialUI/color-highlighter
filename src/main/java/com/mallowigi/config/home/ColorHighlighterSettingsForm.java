/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2022 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

/*
 * Created by JFormDesigner on Sat Sep 25 10:41:27 IDT 2021
 */

package com.mallowigi.config.home;

import com.intellij.openapi.Disposable;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.mallowigi.FeatureLoader;
import com.mallowigi.config.SettingsFormUI;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ResourceBundle;

@SuppressWarnings({"DuplicateStringLiteralInspection",
  "FieldCanBeLocal",
  "InstanceVariableMayNotBeInitialized", "OverlyLongMethod", "StringConcatenation", "BooleanMethodNameMustStartWithQuestion", "FeatureEnvy", "ClassWithTooManyFields"})
public final class ColorHighlighterSettingsForm extends JPanel
  implements SettingsFormUI<ColorHighlighterSettingsForm, ColorHighlighterConfig>, Disposable {

  @Override
  public @NotNull JComponent getContent() {
    return this;
  }

  @Override
  public void init() {
    initComponents();
    toggleFeatures();
  }

  @SuppressWarnings("MethodWithMoreThanThreeNegations")
  private void toggleFeatures() {
    final FeatureLoader featureLoader = FeatureLoader.getInstance();
    if (!featureLoader.isJavaEnabled()) {
      javaPanel.hide();
    }
    if (!featureLoader.isKotlinEnabled()) {
      kotlinPanel.hide();
    }
    if (!featureLoader.isRiderEnabled()) {
      riderPanel.hide();
    }
    if (!featureLoader.isMarkdownEnabled()) {
      markdownPanel.hide();
    }

    // todo remove that once we figured how to tokenize text files
    textPanel.hide();
  }

  @Override
  public void afterStateSet() {
    // do nothing
  }

  @Override
  public void dispose() {
    // do nothing
  }

  @Override
  public void setFormState(final @NotNull ColorHighlighterConfig config) {
    setIsEnabled(config.isEnabled());
    setIsHexDetectEnabled(config.isHexDetectEnabled());
    setIsRgbaEnabled(config.isRgbaEnabled());
    setIsJavaColorCtorEnabled(config.isJavaColorCtorEnabled());
    setIsJavaColorMethodEnabled(config.isJavaColorMethodEnabled());
    setIsKotlinColorCtorEnabled(config.isKotlinColorCtorEnabled());
    setIsKotlinColorMethodEnabled(config.isKotlinColorMethodEnabled());
    setIsRiderColorMethodEnabled(config.isRiderColorMethodEnabled());
    setIsTextEnabled(config.isTextEnabled());
    setIsMarkdownEnabled(config.isMarkdownEnabled());
  }

  @Override
  public boolean isModified(final @NotNull ColorHighlighterConfig config) {
    boolean isModified = config.isEnabledChanged(getIsEnabled());
    isModified = isModified || config.isHexDetectEnabledChanged(getIsHexDetectEnabled());
    isModified = isModified || config.isRgbaEnabledChanged(getIsRgbaEnabled());
    isModified = isModified || config.isJavaColorCtorEnabledChanged(getIsJavaColorCtorEnabled());
    isModified = isModified || config.isJavaColorMethodEnabledChanged(getIsJavaColorMethodEnabled());
    isModified = isModified || config.isKotlinColorCtorEnabledChanged(getIsKotlinColorCtorEnabled());
    isModified = isModified || config.isKotlinColorMethodEnabledChanged(getIsKotlinColorMethodEnabled());
    isModified = isModified || config.isRiderColorMethodEnabledChanged(getIsRiderColorMethodEnabled());
    isModified = isModified || config.isTextEnabledChanged(getIsTextEnabled());
    isModified = isModified || config.isMarkdownEnabledChanged(getIsMarkdownEnabled());
    return isModified;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.ColorHighlighterBundle");
    final DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    globalPanel = new JPanel();
    globalSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.globalSeparator.text"));
    enableCheckbox = new JCheckBox();
    colorParsingCheckbox = new JCheckBox();
    rgbaCheckbox = new JCheckBox();
    javaPanel = new JPanel();
    javaSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.javaSeparator.text"));
    colorCtorCheckbox = new JCheckBox();
    colorMethodCheckbox = new JCheckBox();
    kotlinPanel = new JPanel();
    kotlinSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.kotlinSeparator.text"));
    colorKtCtorCheckbox = new JCheckBox();
    colorKtMethodCheckbox = new JCheckBox();
    riderPanel = new JPanel();
    riderSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.riderSeparator.text"));
    riderColorMethodCheckbox = new JCheckBox();
    textPanel = new JPanel();
    textPanelSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.textPanelSeparator.text"));
    textCheckbox = new JCheckBox();
    markdownPanel = new JPanel();
    markdownSeparator = compFactory.createSeparator(bundle.getString("ColorHighlighterSettingsForm.markdownSeparator.text"));
    markdownCheckbox = new JCheckBox();

    //======== this ========
    setLayout(new MigLayout(
      "fillx,insets panel,hidemode 3,align left top",
      // columns
      "0[left]0",
      // rows
      "0[shrink 0,top]rel" +
        "[shrink 0,top]rel" +
        "[]" +
        "[]"));

    //======== globalPanel ========
    {
      globalPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "0[]" +
          "[]" +
          "[]" +
          "[]"));
      globalPanel.add(globalSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- enableCheckbox ----
      enableCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.enableCheckbox.text"));
      globalPanel.add(enableCheckbox, "cell 0 1,aligny top,growy 0");

      //---- colorParsingCheckbox ----
      colorParsingCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.colorParsingCheckbox.text"));
      colorParsingCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.colorParsingCheckbox.toolTipText"));
      globalPanel.add(colorParsingCheckbox, "cell 0 2,aligny top,growy 0");

      //---- rgbaCheckbox ----
      rgbaCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.rgbaCheckbox.text"));
      rgbaCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.rgbaCheckbox.toolTipText"));
      globalPanel.add(rgbaCheckbox, "cell 0 3,aligny top,growy 0");
    }
    add(globalPanel, "cell 0 0 2 1,aligny top,grow 100 0");

    //======== javaPanel ========
    {
      javaPanel.setBorder(null);
      javaPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "[]" +
          "[]" +
          "[]"));
      javaPanel.add(javaSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- colorCtorCheckbox ----
      colorCtorCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.colorCtorCheckbox.text"));
      colorCtorCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.colorCtorCheckbox.toolTipText"));
      javaPanel.add(colorCtorCheckbox, "cell 0 1,aligny top,growy 0");

      //---- colorMethodCheckbox ----
      colorMethodCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.colorMethodCheckbox.text"));
      colorMethodCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.colorMethodCheckbox.toolTipText"));
      javaPanel.add(colorMethodCheckbox, "cell 0 2,aligny top,growy 0");
    }
    add(javaPanel, "cell 0 2,aligny top,grow 100 0");

    //======== kotlinPanel ========
    {
      kotlinPanel.setBorder(null);
      kotlinPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "[]" +
          "[]" +
          "[]"));
      kotlinPanel.add(kotlinSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- colorKtCtorCheckbox ----
      colorKtCtorCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.colorKtCtorCheckbox.text"));
      colorKtCtorCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.colorKtCtorCheckbox.toolTipText"));
      kotlinPanel.add(colorKtCtorCheckbox, "cell 0 1,aligny top,growy 0");

      //---- colorKtMethodCheckbox ----
      colorKtMethodCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.colorKtMethodCheckbox.text"));
      colorKtMethodCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.colorKtMethodCheckbox.toolTipText"));
      kotlinPanel.add(colorKtMethodCheckbox, "cell 0 2,aligny top,growy 0");
    }
    add(kotlinPanel, "cell 1 2,aligny top,grow 100 0");

    //======== riderPanel ========
    {
      riderPanel.setBorder(null);
      riderPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "[]" +
          "[]"));
      riderPanel.add(riderSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- riderColorMethodCheckbox ----
      riderColorMethodCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.riderColorMethodCheckbox.text"));
      riderColorMethodCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.riderColorMethodCheckbox.toolTipText"));
      riderPanel.add(riderColorMethodCheckbox, "cell 0 1,aligny top,growy 0");
    }
    add(riderPanel, "cell 0 3,aligny top,grow 100 0");

    //======== textPanel ========
    {
      textPanel.setBorder(null);
      textPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "[]" +
          "[]"));
      textPanel.add(textPanelSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- textCheckbox ----
      textCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.textCheckbox.text"));
      textCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.textCheckbox.toolTipText"));
      textPanel.add(textCheckbox, "cell 0 1,aligny top,growy 0");
    }
    add(textPanel, "cell 0 4,aligny top,grow 100 0");

    //======== markdownPanel ========
    {
      markdownPanel.setBorder(null);
      markdownPanel.setLayout(new MigLayout(
        "fillx,hidemode 3,align left top",
        // columns
        "0[left]0",
        // rows
        "[]" +
          "[]"));
      markdownPanel.add(markdownSeparator, "cell 0 0,growx,gapx 0,gapy 10 10");

      //---- markdownCheckbox ----
      markdownCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.markdownCheckbox.text"));
      markdownCheckbox.setToolTipText(bundle.getString("ColorHighlighterSettingsForm.markdownCheckbox.toolTipText"));
      markdownPanel.add(markdownCheckbox, "cell 0 1,aligny top,growy 0");
    }
    add(markdownPanel, "cell 0 1 2 1,aligny top,grow 100 0");
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JPanel globalPanel;
  private JComponent globalSeparator;
  private JCheckBox enableCheckbox;
  private JCheckBox colorParsingCheckbox;
  private JCheckBox rgbaCheckbox;
  private JPanel javaPanel;
  private JComponent javaSeparator;
  private JCheckBox colorCtorCheckbox;
  private JCheckBox colorMethodCheckbox;
  private JPanel kotlinPanel;
  private JComponent kotlinSeparator;
  private JCheckBox colorKtCtorCheckbox;
  private JCheckBox colorKtMethodCheckbox;
  private JPanel riderPanel;
  private JComponent riderSeparator;
  private JCheckBox riderColorMethodCheckbox;
  private JPanel textPanel;
  private JComponent textPanelSeparator;
  private JCheckBox textCheckbox;
  private JPanel markdownPanel;
  private JComponent markdownSeparator;
  private JCheckBox markdownCheckbox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  //region Global Enabled
  public boolean getIsEnabled() {
    return enableCheckbox.isSelected();
  }

  private void setIsEnabled(final boolean isEnabled) {
    enableCheckbox.setSelected(isEnabled);
  }
  //endregion

  //region Hex Detect Enabled
  public boolean getIsHexDetectEnabled() {
    return colorParsingCheckbox.isSelected();
  }

  private void setIsHexDetectEnabled(final boolean isHexDetectEnabled) {
    colorParsingCheckbox.setSelected(isHexDetectEnabled);
  }
  //endregion

  //region RGBA Enabled
  public boolean getIsRgbaEnabled() {
    return rgbaCheckbox.isSelected();
  }

  private void setIsRgbaEnabled(final boolean isRgbaEnabled) {
    rgbaCheckbox.setSelected(isRgbaEnabled);
  }
  //endregion

  //region Java Color Ctor Enabled
  public boolean getIsJavaColorCtorEnabled() {
    return colorCtorCheckbox.isSelected();
  }

  private void setIsJavaColorCtorEnabled(final boolean isJavaColorCtorEnabled) {
    colorCtorCheckbox.setSelected(isJavaColorCtorEnabled);
  }
  //endregion

  //region Java Color Method Enabled
  public boolean getIsJavaColorMethodEnabled() {
    return colorMethodCheckbox.isSelected();
  }

  private void setIsJavaColorMethodEnabled(final boolean isJavaColorMethodEnabled) {
    colorMethodCheckbox.setSelected(isJavaColorMethodEnabled);
  }
  //endregion

  //region Kotlin Color Ctor Enabled
  public boolean getIsKotlinColorCtorEnabled() {
    return colorKtCtorCheckbox.isSelected();
  }

  private void setIsKotlinColorCtorEnabled(final boolean isKotlinColorCtorEnabled) {
    colorKtCtorCheckbox.setSelected(isKotlinColorCtorEnabled);
  }
  //endregion

  //region Kotlin Color Method Enabled
  public boolean getIsKotlinColorMethodEnabled() {
    return colorKtMethodCheckbox.isSelected();
  }

  private void setIsKotlinColorMethodEnabled(final boolean isKotlinColorMethodEnabled) {
    colorKtMethodCheckbox.setSelected(isKotlinColorMethodEnabled);
  }
  //endregion

  //region Rider Color Method Enabled
  public boolean getIsRiderColorMethodEnabled() {
    return riderColorMethodCheckbox.isSelected();
  }

  private void setIsRiderColorMethodEnabled(final boolean isRiderColorMethodEnabled) {
    riderColorMethodCheckbox.setSelected(isRiderColorMethodEnabled);
  }
  //endregion

  //region Text Enabled
  public boolean getIsTextEnabled() {
    return textCheckbox.isSelected();
  }

  private void setIsTextEnabled(final boolean isTextEnabled) {
    textCheckbox.setSelected(isTextEnabled);
  }
  //endregion

  //region Markdown Enabled
  public boolean getIsMarkdownEnabled() {
    return markdownCheckbox.isSelected();
  }

  private void setIsMarkdownEnabled(final boolean isMarkdownEnabled) {
    markdownCheckbox.setSelected(isMarkdownEnabled);
  }
  //endregion

}
