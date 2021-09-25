/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
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
import com.intellij.ui.TitledSeparator;
import com.mallowigi.config.SettingsFormUI;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ResourceBundle;

@SuppressWarnings({"DuplicateStringLiteralInspection",
  "FieldCanBeLocal",
  "InstanceVariableMayNotBeInitialized"})
public final class ColorHighlighterSettingsForm extends JPanel
  implements SettingsFormUI<ColorHighlighterSettingsForm, ColorHighlighterConfig>, Disposable {

  @Override
  public @NotNull JComponent getContent() {
    return this;
  }

  @Override
  public void init() {
    initComponents();
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
  }

  @Override
  public boolean isModified(final @NotNull ColorHighlighterConfig config) {
    return config.isEnabledChanged(getIsEnabled());
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.ColorHighlighterBundle");
    globalSeparator = new TitledSeparator();
    enableCheckbox = new JCheckBox();

    //======== this ========
    setLayout(new MigLayout(
      "fillx,hidemode 3,align left top",
      // columns
      "0[left]0",
      // rows
      "0[]" +
        "[]"));

    //---- globalSeparator ----
    globalSeparator.setText(bundle.getString("ColorHighlighterSettingsForm.globalSeparator.text"));
    add(globalSeparator, "cell 0 0");

    //---- enableCheckbox ----
    enableCheckbox.setText(bundle.getString("ColorHighlighterSettingsForm.enableCheckbox.text"));
    add(enableCheckbox, "cell 0 1,aligny top,growy 0");
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private TitledSeparator globalSeparator;
  private JCheckBox enableCheckbox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  //region File Icons
  public boolean getIsEnabled() {
    return enableCheckbox.isSelected();
  }

  private void setIsEnabled(final boolean enabledIcons) {
    enableCheckbox.setSelected(enabledIcons);
  }
  //endregion
}
