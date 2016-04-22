package com.rtiming.client;

import org.eclipse.scout.rt.client.ui.form.fields.decimalfield.AbstractDecimalField;

/**
 * 
 */
public class AbstractDoubleField extends AbstractDecimalField<Double> {

  @Override
  protected Double getConfiguredMinValue() {
    return Double.MIN_VALUE;
  }

  @Override
  protected Double getConfiguredMaxValue() {
    return Double.MAX_VALUE;
  }

  @Override
  protected Double getMinPossibleValue() {
    return Double.MIN_VALUE;
  }

  @Override
  protected Double getMaxPossibleValue() {
    return Double.MAX_VALUE;
  }

  @Override
  protected Double parseValueInternal(String text) {
    return Double.parseDouble(text);
  }

}
