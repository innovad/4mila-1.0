package com.rtiming.client.common.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.HTMLUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;

/**
 * 
 */
public final class DataSourceUtility {

  public static final String DETAIL = "Detail";
  public static final String COLUMN_HEADER = "ColumnHeader";

  private DataSourceUtility() {
  }

  public static Collection<Map<String, ?>> createCollectionFromTable(ITable table, boolean visibleColumnsOnly, boolean withPrefix) {
    Collection<Map<String, ?>> data = new LinkedList<Map<String, ?>>();
    List<IColumn<?>> columns = table.getColumns();
    if (visibleColumnsOnly) {
      columns = table.getColumnSet().getVisibleColumns();
    }
    // table data
    for (int k = 0; k < table.getRowCount(); k++) {
      if (table.getRow(k).isSelected() || table.getSelectedRowCount() <= 1) {
        Map<String, Object> map = new HashMap<String, Object>();
        data.add(map);

        for (IColumn<?> column : columns) {
          ITableRow row = table.getRow(k);
          String text = null;
          try {
            text = column.getDisplayText(row);
          }
          catch (Exception e) {
            // nop scout bug
          }
          if (StringUtility.isNullOrEmpty(text)) {
            Object value = column.getValue(row);
            if (value != null && value instanceof Boolean) {
              text = (Boolean) value == true ? "X" : "";
            }
            else {
              text = StringUtility.emptyIfNull(value);
            }
          }
          String id = column.getColumnId();
          if (withPrefix) {
            id = DETAIL + "." + id;
          }
          if (text != null && text.contains("<html>") && text.contains("</html>")) {
            text = HTMLUtility.getPlainText(text);
          }
          map.put(id, text);
        }
      }
    }

    return data;
  }

  public static Map<String, Object> createColumnHeadersParametersFromTable(ITable table) {
    Map<String, Object> parameters = new HashMap<>();
    for (IColumn<?> column : table.getColumns()) {
      if (column.isDisplayable()) {
        parameters.put(COLUMN_HEADER + "." + column.getColumnId(), column.getHeaderCell().getText());
      }
    }
    return parameters;
  }

}
