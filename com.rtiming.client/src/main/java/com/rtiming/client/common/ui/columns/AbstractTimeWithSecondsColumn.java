package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public abstract class AbstractTimeWithSecondsColumn extends AbstractDateColumn {

  @Override
  protected void execInitColumn() throws ProcessingException {
    setFormat(FMilaUtility.DEFAULT_TIME_FORMAT_HMS);
  }

  @Override
  protected boolean getConfiguredHasTime() {
    return true;
  }

  @Override
  protected String getConfiguredHeaderText() {
    return Texts.get("StartTime");
  }

  @Override
  protected int getConfiguredWidth() {
    return 80;
  }

}
