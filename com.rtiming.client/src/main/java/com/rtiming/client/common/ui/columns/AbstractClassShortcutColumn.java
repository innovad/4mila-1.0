package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassCodeType;

/**
 * 
 */
public class AbstractClassShortcutColumn extends AbstractSmartColumn<Long> {

  @Override
  protected String getConfiguredHeaderText() {
    return Texts.get("Class");
  }

  @Override
  protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
    return ClassCodeType.class;
  }

  @Override
  protected int getConfiguredWidth() {
    return 60;
  }

  @Override
  protected void execDecorateCell(Cell cell, ITableRow row) throws ProcessingException {
    // only display ext key in column
    Object value = cell.getValue();
    if (value != null) {
      ICode code = BEANS.get(ClassCodeType.class).getCode((Long) value);
      if (code != null) {
        cell.setText(code.getExtKey());
      }
    }
  }

}
