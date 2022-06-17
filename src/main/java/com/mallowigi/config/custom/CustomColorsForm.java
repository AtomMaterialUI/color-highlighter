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
 * Created by JFormDesigner on Sat Sep 25 09:42:05 IDT 2021
 */

package com.mallowigi.config.custom;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.ui.ColumnInfo;
import com.mallowigi.ColorHighlighterBundle;
import com.mallowigi.colors.CustomColors;
import com.mallowigi.colors.SingleColor;
import com.mallowigi.config.SettingsFormUI;
import com.mallowigi.config.ui.columns.ColorEditableColumnInfo;
import com.mallowigi.config.ui.columns.NameEditableColumnInfo;
import com.mallowigi.config.ui.internal.CustomColorsTableItemEditor;
import com.mallowigi.config.ui.internal.CustomColorsTableModelEditor;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ResourceBundle;

@SuppressWarnings({"FieldCanBeLocal",
  "DuplicateStringLiteralInspection",
  "StringConcatenation",
  "InstanceVariableMayNotBeInitialized",
  "ConfusingFloatingPointLiteral",
  "ClassNamePrefixedWithPackageName"})
public final class CustomColorsForm extends JPanel implements SettingsFormUI<CustomColorsForm, CustomColorsConfig>, Disposable {
  private final transient ColumnInfo[] columns = {
    new NameEditableColumnInfo(this, true),
    new ColorEditableColumnInfo(this),
  };

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel explanation;
  private JPanel colorsPanel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  private JComponent customColorsTable;
  private @Nullable CustomColorsTableModelEditor<SingleColor> customColorsEditor;

  @Override
  public void init() {
    initComponents();
    createTables();
  }

  @Override
  public @NotNull JComponent getContent() {
    return this;
  }

  @Override
  public void afterStateSet() {
    // add after state set
  }

  @Override
  public void dispose() {
    customColorsEditor = null;
  }

  @Override
  public void setFormState(final @NotNull CustomColorsConfig config) {
    ApplicationManager.getApplication().invokeLater(() -> {
      if (customColorsEditor != null) {
        customColorsEditor.reset(config.getCustomColors().getTheAssociations());
      }
      afterStateSet();
    });
  }

  @Override
  public boolean isModified(@Nullable final CustomColorsConfig config) {
    if (config == null) {
      return false;
    }

    final boolean modified = customColorsEditor != null && config.isCustomColorsModified(customColorsEditor.getModel().getItems());
    return modified;
  }

  public CustomColors getCustomColors() {
    assert customColorsEditor != null;
    return new CustomColors(customColorsEditor.getModel().getItems());
  }

  private void createTables() {
    createTable();
  }

  private void createTable() {
    final CustomColorsTableItemEditor itemEditor = new CustomColorsTableItemEditor();
    customColorsEditor = new CustomColorsTableModelEditor<>(columns,
      itemEditor,
      ColorHighlighterBundle.message("no.custom.colors"));
    customColorsTable = customColorsEditor.createComponent();
    colorsPanel.add(customColorsTable, "cell 0 0"); //NON-NLS

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.ColorHighlighterBundle");
    explanation = new JLabel();
    colorsPanel = new JPanel();

    //======== this ========
    setBorder(new TitledBorder(null, "Custom Colors Editor", TitledBorder.CENTER, TitledBorder.TOP));
    setLayout(new MigLayout(
      "hidemode 3",
      // columns
      "[369,grow,fill]",
      // rows
      "[]" +
        "[270,grow,fill]" +
        "[]"));

    //---- explanation ----
    explanation.setText(bundle.getString("CustomColorsForm.explanation.text"));
    explanation.setFont(explanation.getFont().deriveFont(explanation.getFont().getSize() - 1f));
    explanation.setForeground(UIManager.getColor("inactiveCaptionText"));
    add(explanation, "cell 0 0");

    //======== colorsPanel ========
    {
      colorsPanel.setLayout(new MigLayout(
        "fill,hidemode 3,aligny top",
        // columns
        "0[grow,fill]0",
        // rows
        "0[grow,top]0"));
    }
    add(colorsPanel, "cell 0 1,grow");
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

}
