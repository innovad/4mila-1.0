package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.shared.TEXTS;

/**
 * Positive Long Field with Maximum Value.
 */
public abstract class AbstractSortCodeField extends AbstractLongField {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("SortCode");
  }

  @Override
  protected Long getConfiguredMaxValue() {
    return 999999999L;
  }

  @Override
  protected Long getConfiguredMinValue() {
    return 0L;
  }

}
