package com.rtiming.client;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractNumberColumn;

/**
 * 
 */
public class AbstractDoubleColumn extends AbstractNumberColumn<Double> {

  @Override
  protected Double getConfiguredMinValue() {
    return Double.MIN_VALUE;
  }

  @Override
  protected Double getConfiguredMaxValue() {
    return Double.MAX_VALUE;
  }

  // TODO MIG
  protected int getConfiguredMinIntegerDigits() {
    return 0;
  }

  // TODO MIG
  protected int getConfiguredMinFractionDigits() {
    return 0;
  }

  // TODO MIG
  protected int getConfiguredMaxFractionDigits() {
    return 0;
  }

}
