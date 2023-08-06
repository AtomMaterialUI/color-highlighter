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
package com.mallowigi.config.ui.internal;

import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.ui.*;
import com.intellij.ui.table.TableView;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.*;
import com.intellij.util.ui.table.ComboBoxTableCellEditor;
import com.intellij.util.xmlb.XmlSerializer;
import com.mallowigi.colors.SingleColor;
import com.mallowigi.utils.ColorUtils;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"SyntheticAccessorCall",
  "AccessingNonPublicFieldOfAnotherObject",
  "MagicNumber",
  "unused"})
public final class CustomColorsTableModelEditor<T> extends CollectionModelEditor<T, CollectionItemEditor<T>> {
  private final TableView<T> table;
  private final ToolbarDecorator toolbarDecorator;

  private final MyListTableModel model;

  public CustomColorsTableModelEditor(final ColumnInfo @NotNull [] columns,
                                      @NotNull final CollectionItemEditor<T> itemEditor,
                                      @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) final String emptyText) {
    this(Collections.emptyList(), columns, itemEditor, emptyText);
  }

  @SuppressWarnings("MagicNumber")
  private CustomColorsTableModelEditor(@NotNull final List<T> items,
                                       final ColumnInfo @NotNull [] columns,
                                       @NotNull final CollectionItemEditor<T> itemEditor,
                                       @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) final String emptyText) {
    super(itemEditor);

    model = new MyListTableModel(columns, new ArrayList<>(items));
    table = new TableView<>(model);
    table.setStriped(true);
    table.setShowColumns(true);
    table.setRowHeight(40);
    table.setMaxItemsForSizeCalculation(20);
    table.setIgnoreRepaint(true);
    table.setFillsViewportHeight(true);
    table.setShowGrid(false);
    table.setDefaultEditor(Enum.class, ComboBoxTableCellEditor.INSTANCE);
    table.setEnableAntialiasing(true);
    table.setPreferredScrollableViewportSize(JBUI.size(200, -1));
    table.setVisibleRowCount(20);
    new TableSpeedSearch(table);

    table.getEmptyText().setFont(UIUtil.getLabelFont().deriveFont(24.0f));
    table.getEmptyText().setText(emptyText);

    // Add actions
    toolbarDecorator = ToolbarDecorator.createDecorator(table, this);

    // Color picker listening
    new CustomColorsClickListener().installOn(table);
  }

  @SuppressWarnings("unused")
  public static <T> void cloneUsingXmlSerialization(@NotNull final T oldItem, @NotNull final T newItem) {
    final Element serialized = XmlSerializer.serialize(oldItem);
    if (serialized != null) {
      XmlSerializer.deserializeInto(newItem, serialized);
    }
  }

  @NotNull
  public CustomColorsTableModelEditor<T> enabled(final boolean value) {
    table.setEnabled(value);
    return this;
  }

  public CustomColorsTableModelEditor<T> modelListener(@NotNull final DataChangedListener<T> listener) {
    model.dataChangedListener = listener;
    model.addTableModelListener(listener);
    return this;
  }

  @NotNull
  public ListTableModel<T> getModel() {
    return model;
  }

  @NotNull
  public JComponent createComponent() {
    return toolbarDecorator.createPanel();
  }

  public void selectItem(@NotNull final T item) {
    table.clearSelection();

    final @Nullable Ref<T> ref;
    if (helper.hasModifiedItems()) {
      ref = Ref.create();
      helper.process((modified, original) -> {
        if (item == original) {
          ref.set(modified);
        }
        return ref.isNull();
      });
    } else {
      ref = null;
    }

    table.addSelection(ref == null || ref.isNull() ? item : ref.get());
  }

  @NotNull
  public List<T> apply() {
    if (helper.hasModifiedItems()) {
      @SuppressWarnings("unchecked") final ColumnInfo<T, Object>[] columns = model.getColumnInfos();
      helper.process((newItem, oldItem) -> {
        for (final ColumnInfo<T, Object> column : columns) {
          if (column.isCellEditable(newItem)) {
            column.setValue(oldItem, column.valueOf(newItem));
          }
        }

        model.items.set(ContainerUtil.indexOfIdentity(model.items, newItem), oldItem);
        return true;
      });
    }

    helper.reset(model.items);
    return model.items;
  }

  @NotNull
  @Override
  protected List<T> getItems() {
    return model.items;
  }

  @Override
  public void reset(@NotNull final List<? extends T> originalItems) {
    super.reset(originalItems);
    model.setItems(new ArrayList<>(originalItems));
  }

  @SuppressWarnings({"AbstractClassNeverImplemented",
    "NoopMethodInAbstractClass"})
  public abstract static class DataChangedListener<T> implements TableModelListener {
    public abstract void dataChanged(@NotNull ColumnInfo<T, ?> columnInfo, int rowIndex);

    @Override
    public void tableChanged(@NotNull final TableModelEvent e) {
    }
  }

  @SuppressWarnings({"FieldHasSetterButNoGetter",
    "SerializableInnerClassWithNonSerializableOuterClass",
    "InstanceVariableMayNotBeInitialized"})
  private final class MyListTableModel extends ListTableModel<T> {
    private List<T> items;
    private DataChangedListener<T> dataChangedListener;

    MyListTableModel(final ColumnInfo @NotNull [] columnNames, @NotNull final List<T> items) {
      super(columnNames, items);

      this.items = items;
    }

    @Override
    public void setItems(@NotNull final List<T> items) {
      this.items = items;
      super.setItems(items);
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
      if (rowIndex < getRowCount()) {
        @SuppressWarnings("unchecked") final ColumnInfo<T, Object> column = getColumnInfos()[columnIndex];
        final T item = getItem(rowIndex);
        final Object oldValue = column.valueOf(item);

        if (column.getColumnClass() == String.class
          ? !Comparing.strEqual(((String) oldValue), ((String) aValue))
          : !Comparing.equal(oldValue, aValue)) {

          column.setValue(helper.getMutable(item, rowIndex), aValue);
          if (dataChangedListener != null) {
            dataChangedListener.dataChanged(column, rowIndex);
          }
        }
      }
    }
  }

  private class CustomColorsClickListener extends ClickListener {
    @Override
    public final boolean onClick(@NotNull final MouseEvent event, final int clickCount) {
      final Point point = event.getPoint();
      final int row = table.rowAtPoint(point);
      final int column = table.columnAtPoint(point);

      if (row >= 0 && row < table.getRowCount() && column == 1) {
        final Object colorValue = model.getValueAt(row, 1);
        final Color modelColor = ColorUtils.INSTANCE.getHex((String) colorValue);

        ColorChooserService.getInstance().showDialog(
          event.getComponent(),
          "Choose Color",
          modelColor,
          false,
          List.of(
            new ColorPickerListener() {
              @Override
              public void colorChanged(final Color color) {
                ((SingleColor) model.items.get(row)).setCode(ColorUtil.toHex(color));
              }

              @Override
              public void closed(@Nullable Color color) {

              }
            })
        );

        return true;
      }
      return false;
    }
  }
}
