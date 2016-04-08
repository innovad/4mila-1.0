package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.ui.TimeDifferenceUtility;

/**
 * 
 */
public class AbstractTimeDifferenceField extends AbstractLongField {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("ShiftTime");
  }

  @Override
  protected String execFormatValue(Long validValue) {
    return TimeDifferenceUtility.formatValue(validValue);
  }

  @Override
  protected Long execParseValue(String text) throws ProcessingException {
    return TimeDifferenceUtility.parseValue(text);
  }

  @Override
  protected Long getConfiguredMaxValue() {
    return 999999999L;
  }

  @Override
  protected Long getConfiguredMinValue() {
    return -999999999L;
  }

}
