package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public abstract class AbstractYearField extends AbstractLongField {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Year");
  }

  @Override
  protected Long getConfiguredMaxValue() {
    return 2999L;
  }

  @Override
  protected Long getConfiguredMinValue() {
    return 1900L;
  }

  @Override
  protected Long validateValueInternal(Long rawValue) throws ProcessingException {
    rawValue = FMilaUtility.validateYear(rawValue);
    return super.validateValueInternal(rawValue);
  }

}
