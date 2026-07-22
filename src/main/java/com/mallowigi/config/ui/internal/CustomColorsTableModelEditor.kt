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
package com.mallowigi.config.ui.internal

import com.intellij.openapi.util.Comparing
import com.intellij.openapi.util.Ref
import com.intellij.ui.ClickListener
import com.intellij.ui.ColorChooserService
import com.intellij.ui.ColorPickerListener
import com.intellij.ui.ColorUtil
import com.intellij.ui.TableSpeedSearch
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.table.TableView
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.ui.CollectionItemEditor
import com.intellij.util.ui.CollectionModelEditor
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ListTableModel
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.table.ComboBoxTableCellEditor
import com.intellij.util.xmlb.XmlSerializer
import com.mallowigi.colors.SingleColor
import com.mallowigi.utils.ColorUtils
import org.jetbrains.annotations.Nls
import java.awt.Color
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener

/**
 * Table model editor for the custom colors settings, backed by a [TableView].
 *
 * @param T the type of the edited items
 * @param columns the columns to display
 * @param itemEditor the item editor used to clone/produce items
 * @param emptyText the text shown when the table is empty
 */
@Suppress("MagicNumber", "unused")
class CustomColorsTableModelEditor<T : Any>(
  columns: Array<ColumnInfo<*, *>>,
  itemEditor: CollectionItemEditor<T>,
  @Nls(capitalization = Nls.Capitalization.Sentence) emptyText: String,
) : CollectionModelEditor<T, CollectionItemEditor<T>>(itemEditor) {

  private val model: MyListTableModel = MyListTableModel(columns, ArrayList())
  private val table: TableView<T> = TableView(model)
  private val toolbarDecorator: ToolbarDecorator

  init {
    table.isStriped = true
    table.setShowColumns(true)
    table.rowHeight = 40
    table.setMaxItemsForSizeCalculation(20)
    table.ignoreRepaint = true
    table.fillsViewportHeight = true
    table.setShowGrid(false)
    table.setDefaultEditor(Enum::class.java, ComboBoxTableCellEditor.INSTANCE)
    table.setEnableAntialiasing(true)
    table.preferredScrollableViewportSize = JBUI.size(200, -1)
    table.visibleRowCount = 20
    TableSpeedSearch.installOn(table)

    table.emptyText.setFont(UIUtil.getLabelFont().deriveFont(24.0f))
    table.emptyText.text = emptyText

    // Add actions
    toolbarDecorator = ToolbarDecorator.createDecorator(table, this)

    // Color picker listening
    CustomColorsClickListener().installOn(table)
  }

  fun enabled(value: Boolean): CustomColorsTableModelEditor<T> {
    table.isEnabled = value
    return this
  }

  fun modelListener(listener: DataChangedListener<T>): CustomColorsTableModelEditor<T> {
    model.dataChangedListener = listener
    model.addTableModelListener(listener)
    return this
  }

  fun getModel(): ListTableModel<T> = model

  fun createComponent(): JComponent = toolbarDecorator.createPanel()

  fun selectItem(item: T) {
    table.clearSelection()

    val ref: Ref<T>? = if (helper.hasModifiedItems()) {
      Ref.create<T>().also { r ->
        helper.process { modified, original ->
          if (item === original) {
            r.set(modified)
          }
          r.isNull
        }
      }
    } else {
      null
    }

    table.addSelection(if (ref == null || ref.isNull) item else ref.get())
  }

  fun apply(): List<T> {
    if (helper.hasModifiedItems()) {
      @Suppress("UNCHECKED_CAST")
      val columns = model.columnInfos as Array<ColumnInfo<T, Any?>>
      helper.process { newItem, oldItem ->
        for (column in columns) {
          if (column.isCellEditable(newItem)) {
            column.setValue(oldItem, column.valueOf(newItem))
          }
        }

        model.items[ContainerUtil.indexOfIdentity(model.items, newItem)] = oldItem
        true
      }
    }

    helper.reset(model.items)
    return model.items
  }

  override fun getItems(): List<T> = model.items

  override fun reset(originalItems: List<T>) {
    super.reset(originalItems)
    model.items = ArrayList(originalItems)
  }

  abstract class DataChangedListener<T> : TableModelListener {
    abstract fun dataChanged(columnInfo: ColumnInfo<T, *>, rowIndex: Int)

    override fun tableChanged(e: TableModelEvent) {
      // Do nothing
    }
  }

  private inner class MyListTableModel(
    columnNames: Array<ColumnInfo<*, *>>,
    items: MutableList<T>,
  ) : ListTableModel<T>(columnNames, items) {

    var dataChangedListener: DataChangedListener<T>? = null

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
      if (rowIndex >= rowCount) return

      @Suppress("UNCHECKED_CAST")
      val column = columnInfos[columnIndex] as ColumnInfo<T, Any?>
      val item = getItem(rowIndex)
      val oldValue = column.valueOf(item)

      val changed = if (column.columnClass == String::class.java) {
        !Comparing.strEqual(oldValue as? String, aValue as? String)
      } else {
        !Comparing.equal(oldValue, aValue)
      }

      if (changed) {
        column.setValue(helper.getMutable(item, rowIndex), aValue)
        dataChangedListener?.dataChanged(column, rowIndex)
      }
    }
  }

  private inner class CustomColorsClickListener : ClickListener() {
    override fun onClick(event: MouseEvent, clickCount: Int): Boolean {
      val point = event.point
      val row = table.rowAtPoint(point)
      val column = table.columnAtPoint(point)

      if (row in 0 until table.rowCount && column == 1) {
        val colorValue = model.getValueAt(row, 1) as String
        val modelColor = ColorUtils.getHex(colorValue)

        ColorChooserService.getInstance().showDialog(
          event.component,
          "Choose Color",
          modelColor,
          false,
          listOf(
            object : ColorPickerListener {
              override fun colorChanged(color: Color) {
                (model.items[row] as SingleColor).code = ColorUtil.toHex(color)
              }

              override fun closed(color: Color?) {
                // Do nothing
              }
            },
          ),
        )

        return true
      }
      return false
    }
  }

  companion object {
    @JvmStatic
    fun <T : Any> cloneUsingXmlSerialization(oldItem: T, newItem: T) {
      val serialized = XmlSerializer.serialize(oldItem)
      if (serialized != null) {
        XmlSerializer.deserializeInto(newItem, serialized)
      }
    }
  }
}
