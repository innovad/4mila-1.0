package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;

import com.rtiming.shared.FMilaUtility;

public abstract class AbstractDateTimeWithSecondsColumn extends AbstractDateColumn {

  @Override
  public String getFormat() {
    return FMilaUtility.DEFAULT_TIME_FORMAT_DATE_HMS;
  }

  @Override
  protected boolean getConfiguredHasTime() {
    return true;
  }

  @Override
  protected int getConfiguredWidth() {
    return 80;
  }

}
