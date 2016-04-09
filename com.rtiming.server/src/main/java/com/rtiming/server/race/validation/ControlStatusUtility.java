package com.rtiming.server.race.validation;

import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * 
 */
public final class ControlStatusUtility {

  public static void setControlStatus(RaceControlBean control, Long targetStatusUid) {
    if (control == null || targetStatusUid == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    if (!control.isManualStatus()) {
      control.setControlStatusUid(targetStatusUid);
    }
  }

  public static long calculateRaceStatusFromManualControlStatus(long raceStatusUid, RaceControlBean planned) {
    if (planned == null) {
      throw new IllegalArgumentException("Planned control must be set");
    }
    if (CompareUtility.notEquals(ControlStatusCodeType.OkCode.ID, planned.getControlStatusUid()) &&
        planned.isMandatory()) {
      // Set race status to MP if control status is not OK and control is mandatory
      raceStatusUid = (raceStatusUid == RaceStatusCodeType.OkCode.ID ? RaceStatusCodeType.MissingPunchCode.ID : raceStatusUid);
    }
    return raceStatusUid;
  }

  public static long setMissingControlStatus(RaceControlBean missing, long previousRaceStatusUid) {
    if (missing == null) {
      throw new IllegalArgumentException("Control must not be null");
    }
    long resultingRaceStatusUid = previousRaceStatusUid;
    if (missing.isMandatory()) {
      // mandatory control > control is missing
      missing.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
      if (resultingRaceStatusUid == RaceStatusCodeType.OkCode.ID) {
        resultingRaceStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
      }
    }
    else {
      // control not mandatory > set default status
      missing.setControlStatusUid(ControlStatusCodeType.InitialStatusCode.ID);
    }
    return resultingRaceStatusUid;
  }

}
