package com.rtiming.server.race;

import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.server.race.validation.ControlStatusUtility;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * 
 */
public class RaceTimeUtility {

  public static RaceValidationResult calculateTimes(List<RaceControlBean> plannedControls, Long startTime, Long finishTime, long raceStatusUid, Long timePrecisionUid) throws VetoException {
    if (plannedControls == null) {
      throw new IllegalArgumentException("Controls must not be null");
    }

    // do leg and overall time calculation
    long ignoredLegTimeSum = 0; // sum of legs with ignored flag
    Long lastTime = null;
    if (startTime != null) {
      lastTime = 0L;
    }

    for (RaceControlBean plannedControl : plannedControls) {
      long shiftTime = NumberUtility.nvl(plannedControl.getShiftTime(), 0);

      // Start
      if (CompareUtility.equals(plannedControl.getTypeUid(), ControlTypeCodeType.StartCode.ID)) {
        if (startTime == null) {
          updateRaceControlTime(plannedControl, null, null);
          ControlStatusUtility.setControlStatus(plannedControl, ControlStatusCodeType.MissingCode.ID);
        }
        else {
          startTime += shiftTime;
          updateRaceControlTime(plannedControl, 0L, 0L);
          ControlStatusUtility.setControlStatus(plannedControl, ControlStatusCodeType.OkCode.ID);
        }
      }
      // Finish
      else if (CompareUtility.equals(plannedControl.getTypeUid(), ControlTypeCodeType.FinishCode.ID)) {
        Long overallTime = null;
        Long legTime = null;
        if (finishTime != null) {
          finishTime += shiftTime;
        }
        if (finishTime != null && startTime != null) {
          overallTime = finishTime - startTime - ignoredLegTimeSum;
          legTime = calculateLegTime(lastTime, overallTime, timePrecisionUid);
          if (!plannedControl.isCountLeg() && legTime != null) {
            // if leg does not count
            // add leg time to sum which will be subtracted from overall/total times
            ignoredLegTimeSum += legTime;
            overallTime -= legTime;
          }
        }
        updateRaceControlTime(plannedControl, overallTime, legTime);
        if (finishTime == null) {
          ControlStatusUtility.setControlStatus(plannedControl, ControlStatusCodeType.MissingCode.ID);
        }
        else {
          ControlStatusUtility.setControlStatus(plannedControl, ControlStatusCodeType.OkCode.ID);
        }
      }
      // Control
      else if (CompareUtility.equals(plannedControl.getTypeUid(), ControlTypeCodeType.ControlCode.ID)) {
        Long overallTime = null;
        Long legTime = null;
        Long punchTime = plannedControl.getPunchTime();

        if (punchTime != null) {
          punchTime += shiftTime;
          // nvl is a workaround since we have to calculate leg times even if there is no start time
          // overall times are set to null in the last step if there is no start time
          overallTime = punchTime - NumberUtility.nvl(startTime, 0) - ignoredLegTimeSum;
        }

        // ok
        if (CompareUtility.equals(plannedControl.getControlStatusUid(), ControlStatusCodeType.OkCode.ID)) {
          if (overallTime != null && lastTime != null) {
            legTime = calculateLegTime(lastTime, overallTime, timePrecisionUid);
            if (!plannedControl.isCountLeg() && legTime != null) {
              // if leg does not count
              // add leg time to sum which will be subtracted from overall/total times
              ignoredLegTimeSum += legTime;
              overallTime -= legTime;
            }
          }
          if (overallTime != null) {
            lastTime = overallTime;
          }
        }

        updateRaceControlTime(plannedControl, overallTime, legTime);
      }
    }

    Long legTime = null;
    if (startTime != null && finishTime != null) {
      legTime = finishTime - startTime - ignoredLegTimeSum;
      if (legTime < 0) {
        throw new VetoException(TEXTS.get("RaceValidationNegativeTimeMessage"));
      }
    }

    if (startTime == null) {
      // no start time, therefore force no start time
      raceStatusUid = RaceStatusCodeType.NoStartTimeCode.ID;
      for (RaceControlBean plannedControl : plannedControls) {
        plannedControl.setOverallTime(null);
      }
    }

    return new RaceValidationResult(plannedControls, legTime, startTime, raceStatusUid, ignoredLegTimeSum);
  }

  public static void updateRaceControlTime(RaceControlBean bean, Long overall, Long leg) {
    if (bean == null) {
      throw new IllegalArgumentException("bean should not be null");
    }
    bean.setOverallTime(overall);
    bean.setLegTime(leg);
  }

  private static Long calculateLegTime(Long lastTime, Long overallTime, Long timePrecisionUid) {
    Long time = FMilaUtility.roundTime(overallTime, timePrecisionUid) - FMilaUtility.roundTime(lastTime, timePrecisionUid);
    if (time >= 0) {
      return time;
    }
    // no negative leg times
    return null;
  }

}
