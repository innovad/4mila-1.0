package com.rtiming.client.common.ui.fields;

import com.rtiming.client.AbstractDoubleField;

public abstract class AbstractLongLatField extends AbstractDoubleField {

  @Override
  protected int getConfiguredMaxFractionDigits() {
    return 10;
  }

  @Override
  protected int getConfiguredMinFractionDigits() {
    return 10;
  }

  @Override
  protected int getConfiguredFractionDigits() {
    return 10;
  }

}
