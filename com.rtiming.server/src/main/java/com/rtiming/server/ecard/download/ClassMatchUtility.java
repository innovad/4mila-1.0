package com.rtiming.server.ecard.download;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.TriState;
import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.event.course.loop.CourseCalculator;
import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceSettings;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.entry.AgeUtility;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.race.RaceStatusCodeType;

public class ClassMatchUtility {

  private ClassMatchUtility() {
  }

  public static Set<EventClassFormData> matchClasses(Long eventNr, Long sexUid, Long year, EventConfiguration eventConfiguration, List<RtClassAge> ageConfig) throws ProcessingException {
    if (eventNr == null || eventConfiguration == null || ageConfig == null) {
      throw new IllegalArgumentException("mandatory arguements missing");
    }
    Set<EventClassFormData> result = new HashSet<>();

    // filter classes for event
    EventBean event = eventConfiguration.getEvent(eventNr);
    if (event != null) {
      List<EventClassFormData> clazzes = eventConfiguration.getClasses();
      for (EventClassFormData clazz : clazzes) {
        if (clazz.getEvent().getValue() == event.getEventNr() &&
            clazz.getClientNr() == event.getClientNr()) {

          TriState triState = AgeUtility.isRunnerValidForClassAge(event.getEvtZero(), clazz.getClazz().getValue(), clazz.getClientNr(), sexUid, year, ageConfig);
          if (!TriState.FALSE.equals(triState)) {
            result.add(clazz);
          }

        }
      }
    }

    return result;
  }

  public static ClazzMatchCandidate match(List<ClazzMatchCandidate> candidates, List<PunchFormData> punches) throws ProcessingException {
    if (punches == null) {
      return null;
    }

    RaceSettings settings = new RaceSettings();
    settings.setUsingStartlist(false);

    List<RaceControlBean> punchedControls = new ArrayList<>();
    for (PunchFormData punch : punches) {
      RaceControlBean bean = new RaceControlBean();
      bean.setControlNo(punch.getControlNo().getValue());
      bean.setSortcode(punch.getSortCode().getValue());
      punchedControls.add(bean);
    }

    Map<ClazzMatchCandidate, RaceValidationResult> result = new HashMap<>();

    for (ClazzMatchCandidate candidate : candidates) {
      List<List<CourseControlRowData>> courses = candidate.getCourses();
      for (List<CourseControlRowData> course : courses) {
        RaceValidationResult validation = CourseCalculator.validateAllVariants(punchedControls, course, null, settings, 0L, Long.MAX_VALUE, 0L, 0L);
        if (validation.getStatusUid() == RaceStatusCodeType.OkCode.ID) {
          return candidate;
        }
        // replace if there are multiple variants
        if (result.get(candidate) != null) {
          Long existingControlCount = countCorrectControls(result.get(candidate));
          Long currentControlCount = countCorrectControls(validation);
          if (currentControlCount > existingControlCount) {
            result.put(candidate, validation);
          }
        }
        result.put(candidate, validation);
      }
    }

    // Comparator
    Comparator<? super ClazzMatchResult> comparator = new Comparator<ClazzMatchResult>() {
      @Override
      public int compare(ClazzMatchResult o1, ClazzMatchResult o2) {
        if (o1 != null && o2 != null) {
          return -1 * o1.getControlCount().compareTo(o2.getControlCount());
        }
        else if (o1 != null) {
          return 1;
        }
        else if (o2 != null) {
          return -1;
        }
        return 0;
      }
    };

    // order validation results and return best candidate
    List<ClazzMatchResult> list = new ArrayList<>();
    for (ClazzMatchCandidate key : result.keySet()) {
      RaceValidationResult validation = result.get(key);
      ClazzMatchResult matchResult = new ClazzMatchResult(key, countCorrectControls(validation));
      list.add(matchResult);
    }
    Collections.sort(list, comparator);
    if (list.size() > 0) {
      return list.get(0).getCandidate();
    }
    return null;
  }

  private static Long countCorrectControls(RaceValidationResult validation) {
    Long result = 0L;
    for (RaceControlBean control : validation.getControls()) {
      if (CompareUtility.equals(control.getControlStatusUid(), ControlStatusCodeType.OkCode.ID)) {
        result++;
      }
    }
    return result;
  }

}
