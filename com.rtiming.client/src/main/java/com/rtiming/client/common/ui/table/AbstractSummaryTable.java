package com.rtiming.client.common.ui.table;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;

import com.rtiming.client.AbstractDoubleColumn;
import com.rtiming.shared.Texts;

public abstract class AbstractSummaryTable extends AbstractTable {

  protected abstract Class<? extends AbstractBooleanColumn> getConfiguredSummaryOrderColumn();

  protected abstract Class<? extends AbstractStringColumn> getConfiguredSummaryTotalColumn();

  @Override
  protected final void execDecorateRow(ITableRow row) throws ProcessingException {

    // create summary row
    if ((getTableData().length - 1) == row.getRowIndex() && !BooleanUtility.nvl(getColumnSet().getColumnByClass(getConfiguredSummaryOrderColumn()).getValue(row), false)) {
      ITableRow newRow = createRow();
      getColumnSet().getColumnByClass(getConfiguredSummaryOrderColumn()).setValue(newRow, true);
      addRow(newRow);
    }

    // populate summary row
    if (BooleanUtility.nvl(getColumnSet().getColumnByClass(getConfiguredSummaryOrderColumn()).getValue(row), false)) {
      // bold
      row.setFont(FontSpec.parse("bold"));
      row.setIconId(null);

      // label
      getColumnSet().getColumnByClass(getConfiguredSummaryTotalColumn()).setValue(row, Texts.get("Total"));

      // summary
      int dataColCount = getRowCount() - 1;
      for (IColumn column : getColumnSet().getColumns()) {
        if (column.isDisplayable() && !(column instanceof IIgnoreSummaryColumn)) {
          // Long
          if (column instanceof AbstractLongColumn) {
            Long sum = 0L;
            for (int k = 0; k < dataColCount; k++) {
              sum += NumberUtility.nvl((Long) getTableData()[k][column.getColumnIndex()], 0);
            }
            ((AbstractLongColumn) column).setValue(dataColCount, sum);
          }

          // Double
          if (column instanceof AbstractDoubleColumn) {
            Double sum = 0D;
            for (int k = 0; k < dataColCount; k++) {
              sum = sum + NumberUtility.nvl((Double) getTableData()[k][column.getColumnIndex()], 0D);
            }
            ((AbstractDoubleColumn) column).setValue(dataColCount, sum);
          }
        }
      }
    }
  }

}
