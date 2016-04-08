package com.rtiming.client.result.split;

import com.rtiming.client.result.Control;

public class SplitTime {

  private final LegTime legTime;
  private final OverallTime overallTime;
  private final Control control;
  private final Integer originalSortCode;

  public SplitTime(Control control, LegTime legTime, OverallTime overallTime, Integer originalSortCode) {
    super();
    this.control = control;
    this.legTime = legTime;
    this.overallTime = overallTime;
    this.originalSortCode = originalSortCode;
  }

  public LegTime getLegTime() {
    return legTime;
  }

  public OverallTime getOverallTime() {
    return overallTime;
  }

  public Integer getOriginalSortCode() {
    return originalSortCode;
  }

  public Control getControl() {
    return control;
  }

}
