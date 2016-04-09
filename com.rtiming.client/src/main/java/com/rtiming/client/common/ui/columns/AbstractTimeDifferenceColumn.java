package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.ui.TimeDifferenceUtility;

/**
 * 
 */
public class AbstractTimeDifferenceColumn extends AbstractLongColumn {

  @Override
  protected void execDecorateCell(Cell cell, ITableRow row) throws ProcessingException {
    if (cell.getValue() != null && cell.getValue() instanceof Long) {
      Long value = (Long) cell.getValue();
      String formatted = TimeDifferenceUtility.formatValue(value);
      cell.setText(formatted);
    }
  }

}
