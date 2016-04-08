package com.rtiming.client.result;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;

import com.rtiming.shared.Texts;

public abstract class AbstractControlColumn extends AbstractStringColumn {

  @Override
  protected String getConfiguredHeaderText() {
    return Texts.get("Control");
  }

  @Override
  protected int getConfiguredWidth() {
    return 70;
  }

  @Override
  protected int getConfiguredHorizontalAlignment() {
    return 1;
  }

}
