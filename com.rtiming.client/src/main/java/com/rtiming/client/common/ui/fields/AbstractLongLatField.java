package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;

public abstract class AbstractLongLatField extends AbstractBigDecimalField {

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
