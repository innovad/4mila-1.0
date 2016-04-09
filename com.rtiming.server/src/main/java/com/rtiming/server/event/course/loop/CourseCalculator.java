package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceSettings;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.server.race.validation.RaceValidationUtility;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

/**
 * 
 */
public class CourseCalculator {

  public static List<List<CourseControlRowData>> calculateCourse(List<CourseControlRowData> definitions) {
    List<List<CourseControlRowData>> result = new ArrayList<>();
    List<List<CourseControlRowData>> forks = CourseLoopCalculator.calculateCourse(definitions, CourseForkTypeCodeType.ForkCode.ID);
    for (List<CourseControlRowData> fork : forks) {
      result.addAll(CourseLoopCalculator.calculateCourse(fork, CourseForkTypeCodeType.ButterflyCode.ID));
    }
    return result;
  }

  public static RaceValidationResult validateAllVariants(List<RaceControlBean> punchedControls,
      List<CourseControlRowData> courseControls,
      List<RaceControlBean> manualControls,
      RaceSettings settings, Long startTime, Long finishTime, Long legStartTime, Long punchSessionNr) throws ProcessingException {
    if (punchedControls == null || courseControls == null || settings == null) {
      throw new IllegalArgumentException("Mandatory arguments required.");
    }
    RaceValidationResult validationResult = null;
    List<RaceControlBean> plannedControls;
    List<List<CourseControlRowData>> result = calculateCourse(courseControls);
    // validate all variants and get first valid
    TreeMap<Long, RaceValidationResult> bestWorstResult = new TreeMap<>();
    for (List<CourseControlRowData> courseVariant : result) {
      plannedControls = CourseLoopCalculator.convertToRaceControls(courseVariant, settings.getEventNr());
      if (manualControls != null && manualControls.size() > 0) {
        // there are manual controls, update new planned control with shift time and manual status
        for (RaceControlBean manualControl : manualControls) {
          if (manualControl.isManualStatus() || NumberUtility.nvl(manualControl.getShiftTime(), 0) != 0) {
            for (RaceControlBean plannedControl : plannedControls) {
              if (CompareUtility.equals(plannedControl.getCourseControlNr(), manualControl.getCourseControlNr())) {
                plannedControl.setManualStatus(manualControl.isManualStatus());
                plannedControl.setShiftTime(manualControl.getShiftTime());
                plannedControl.setControlStatusUid(manualControl.getControlStatusUid());
                plannedControl.setPunchTime(manualControl.getPunchTime());
              }
            }
          }
        }
      }
      validationResult = RaceValidationUtility.validateRace(punchSessionNr, plannedControls, punchedControls, startTime, legStartTime, finishTime, settings);
      if (validationResult.getStatusUid() == RaceStatusCodeType.OkCode.ID) {
        // return first OK result
        return validationResult;
      }
      else {
        long okControlCount = 0;
        for (RaceControlBean control : validationResult.getControls()) {
          if (CompareUtility.equals(control.getControlStatusUid(), ControlStatusCodeType.OkCode.ID)) {
            okControlCount++;
          }
        }
        bestWorstResult.put(okControlCount, validationResult);
      }
    }
    // if no OK result, return best MP result (max. amount of OK punches)
    return bestWorstResult.get(bestWorstResult.lastKey());
  }

}
