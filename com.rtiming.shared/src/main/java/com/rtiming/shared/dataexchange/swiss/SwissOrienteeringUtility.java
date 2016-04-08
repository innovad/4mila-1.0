package com.rtiming.shared.dataexchange.swiss;

import org.eclipse.scout.commons.StringUtility;

public final class SwissOrienteeringUtility {

  private SwissOrienteeringUtility() {
  }

  public static boolean isNullOrEmptyOrDash(String string) {

    if (StringUtility.isNullOrEmpty(string)) {
      return true;
    }
    if ("-".equals(string)) {
      return true;
    }
    return false;

  }

}
