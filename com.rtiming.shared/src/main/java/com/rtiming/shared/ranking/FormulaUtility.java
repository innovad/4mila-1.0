package com.rtiming.shared.ranking;

import org.eclipse.scout.commons.CompareUtility;

import com.rtiming.shared.race.TimePrecisionCodeType;

public final class FormulaUtility {

  private FormulaUtility() {
  }

  public static Long decimalPlaces2timePrecision(Long decimalPlaces) {
    if (decimalPlaces == null) {
      return TimePrecisionCodeType.Precision1sCode.ID;
    }
    else if (decimalPlaces == 0) {
      return TimePrecisionCodeType.Precision1sCode.ID;
    }
    else if (decimalPlaces == 1) {
      return TimePrecisionCodeType.Precision10sCode.ID;
    }
    else if (decimalPlaces == 2) {
      return TimePrecisionCodeType.Precision100sCode.ID;
    }
    return TimePrecisionCodeType.Precision1000sCode.ID;
  }

  public static Long timePrecision2decimalPlaces(Long timePrecision) {
    if (CompareUtility.equals(TimePrecisionCodeType.Precision1sCode.ID, timePrecision)) {
      return 0L;
    }
    else if (CompareUtility.equals(TimePrecisionCodeType.Precision10sCode.ID, timePrecision)) {
      return 1L;
    }
    else if (CompareUtility.equals(TimePrecisionCodeType.Precision100sCode.ID, timePrecision)) {
      return 2L;
    }
    else if (CompareUtility.equals(TimePrecisionCodeType.Precision1000sCode.ID, timePrecision)) {
      return 3L;
    }
    return 0L;
  }
}
