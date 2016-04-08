package com.rtiming.shared.common.security;

import java.util.Date;

import org.eclipse.scout.rt.platform.util.date.DateUtility;

public class FMilaVersion {

  public FMilaVersion(String version) {
  }

  public Date getDate() {
    Date date = null;
    try {
// TODO MIG      date = DateUtility.parse(getQualifier(), "yyyyMMddHHmm");
    }
    catch (Exception e) {
      // nop
    }
    return date;
  }

  public String getSimpleVersion() {
    // TODO MIG    return getMajor() + "." + getMinor() + "." + getMicro();
    return null;
  }

  @Override
  public String toString() {
    if (getDate() == null) {
      return super.toString();
    }
    else {
      return getSimpleVersion() + " (" + DateUtility.formatDateTime(getDate()) + ")";
    }
  }

}
