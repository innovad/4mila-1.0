package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceSettings;
import com.rtiming.server.race.RaceTimeUtility;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

public final class RaceValidationUtility {

  private RaceValidationUtility() {
  }

  /**
   * @param punched
   *          the punched control (replacement controls are ignored here)
   * @param planned
   *          the planned control (including replacement controls)
   * @return true if both controls are equal (same control number or allowed replacement control)
   */
  public static boolean areEqual(RaceControlBean punched, RaceControlBean planned) {
    if (punched != null && planned != null) {
      if (StringUtility.equalsIgnoreCase(punched.getControlNo(), planned.getControlNo())) {
        return true;
      }
      if (planned.getReplacementControlNos().contains(punched.getControlNo())) {
        return true;
      }
    }
    return false;
  }

  public static RaceValidationResult validateRace(
      Long punchSessionNr,
      List<RaceControlBean> plannedControls,
      List<RaceControlBean> punchedControls,
      Long punchStartTime,
      Long legStartTime,
      Long finishTime,
      RaceSettings settings) throws ProcessingException {

    // check
    if (punchSessionNr == null) {
      throw new VetoException(TEXTS.get("NoECardAssignedMessage"));
    }
    if (settings == null) {
      throw new IllegalArgumentException("Mandatory Arguements missing");
    }

    // start time in case of start list
    if (settings.isUsingStartlist()) {
      punchStartTime = legStartTime;
    }

    // validation
    return validateControls(plannedControls, punchedControls, punchStartTime, finishTime, settings.getTimePrecisionUid());
  }

  protected static RaceValidationResult validateControls(List<RaceControlBean> plannedControls, List<RaceControlBean> punchedControls, Long startTime, Long finishTime, Long timePrecisionUid) throws ProcessingException {
    if (plannedControls == null || punchedControls == null) {
      throw new IllegalArgumentException("Controls must not be null");
    }
    // make a copy since the list is changed by this method
    punchedControls = new ArrayList<>(punchedControls);

    long raceStatusUid = RaceStatusCodeType.OkCode.ID;

    // freeorder
    Long lastSortCode = null;
    List<RaceControlBean> freeOrderGroup = new ArrayList<RaceControlBean>();
    List<Long> freeOrderSortCodes = new ArrayList<Long>();
    for (RaceControlBean planned : plannedControls) {
      Long sortCode = planned.getSortcode();
      if (CompareUtility.notEquals(sortCode, lastSortCode)) {
        // a free order group just ended
        FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
        if (result.getFreeOrderSortCode() != null) {
          freeOrderSortCodes.add(result.getFreeOrderSortCode());
          raceStatusUid = raceStatusUid == RaceStatusCodeType.OkCode.ID ? result.getRaceStatusUid() : raceStatusUid;
        }
      }
      freeOrderGroup.add(planned);
      lastSortCode = sortCode;
    }
    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    if (result.getFreeOrderSortCode() != null) {
      freeOrderSortCodes.add(result.getFreeOrderSortCode());
      raceStatusUid = raceStatusUid == RaceStatusCodeType.OkCode.ID ? result.getRaceStatusUid() : raceStatusUid;
    }

    // now we have the time, sort freeorder controls
    FreeOrderUtility.sortControlsBySortCodeAndTime(plannedControls);

    // filter for controls, without start and finish types
    List<RaceControlBean> plannedControlsWithoutStartFinish = new ArrayList<>();
    lastSortCode = null;
    for (RaceControlBean bean : plannedControls) {
      if (CompareUtility.equals(bean.getTypeUid(), ControlTypeCodeType.ControlCode.ID)) {
        plannedControlsWithoutStartFinish.add(bean);
      }
      else if (bean.getTypeUid() == null) {
        throw new IllegalArgumentException("control must have a type for validation");
      }
      lastSortCode = bean.getSortcode();
    }

    // levenshtein distance
    int[][] lcssMatrix = LevenshteinUtility.calculateMatrix(plannedControlsWithoutStartFinish, punchedControls);
    LevenshteinResult backtrace = LevenshteinUtility.backtrace(plannedControlsWithoutStartFinish, punchedControls, lcssMatrix);
    List<RaceControlBean> additional = backtrace.getAdditionalControls();
    raceStatusUid = raceStatusUid != RaceStatusCodeType.OkCode.ID ? raceStatusUid : backtrace.getRaceStatusUid();

    // add additional controls at the end
    plannedControls.addAll(additional);

    return RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, timePrecisionUid);
  }

  public static Long updateRaceControlStatus(RaceControlBean bean, Long previousRaceStatusUid, Long plannedRaceStatusUid, long controlStatusUid) throws ProcessingException {
    if (bean == null) {
      throw new IllegalArgumentException("bean should not be null");
    }
    if (bean.isManualStatus()) {
      // PrevRace = Ok && Control = Ok => Ok
      // PrevRace = NOk && Control = Ok => NOk
      // PrevRace = Ok && Control = NOk => NOk
      // PrevRace = NOk && Control = NOk => NOk;
      if (CompareUtility.equals(bean.getControlStatusUid(), ControlStatusCodeType.OkCode.ID) &&
          CompareUtility.equals(previousRaceStatusUid, RaceStatusCodeType.OkCode.ID)) {
        plannedRaceStatusUid = RaceStatusCodeType.OkCode.ID;
      }
      else {
        plannedRaceStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
      }
    }
    else {
      bean.setControlStatusUid(controlStatusUid);
    }
    return plannedRaceStatusUid;
  }

}
