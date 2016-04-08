package com.rtiming.client.event.course;

import com.rtiming.shared.event.course.ControlTypeCodeType;

public class ControlFormUtility {

  private ControlFormUtility() {
  }

  public static Long parseControlType(String controlNo) {
    if (controlNo != null) {
      if (controlNo.toUpperCase().startsWith("S")) {
        return ControlTypeCodeType.StartCode.ID;
      }
      else if (controlNo.toUpperCase().startsWith("F")) {
        return ControlTypeCodeType.FinishCode.ID;
      }
      else if (controlNo.toUpperCase().startsWith("Z")) {
        return ControlTypeCodeType.FinishCode.ID;
      }
    }
    return ControlTypeCodeType.ControlCode.ID;
  }

}
