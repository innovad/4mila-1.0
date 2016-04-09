package com.rtiming.client.result.split;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.result.Control;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.event.course.ControlStatusCodeType;

/**
 * 
 */
public class SplitCalculator {

  public static Map<Long /* raceNr */, List<SplitTime>> calculateSplits(Long winnerRaceNr, Long timePrecisionUid, Long... raceNrs) throws ProcessingException {
    // load splits and controls for all races/runner
    List<RaceControlRowData> splits = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(ClientSession.get().getSessionClientNr(), raceNrs);
    return calculate(winnerRaceNr, timePrecisionUid, splits);
  }

  public static Map<Long /* raceNr */, List<SplitTime>> calculate(Long winnerRaceNr, Long timePrecisionUid, List<RaceControlRowData> splits) throws ProcessingException {

    // split info for each runner
    Map<Long /* raceNr */, List<SplitTime>> data = new HashMap<>();
    Map<Control, List<LegTime>> legAnalysis = new HashMap<>();
    Map<Control, List<OverallTime>> overallAnalysis = new HashMap<>();

    Long currentRaceNr = null;
    List<SplitTime> list = new ArrayList<SplitTime>();
    Map<String, Long> controlNoCounter = new HashMap<>();
    Long lastSplitStatus = ControlStatusCodeType.OkCode.ID;
    for (RaceControlRowData row : splits) {
      if (CompareUtility.notEquals(currentRaceNr, row.getRaceNr())) {
        if (list.size() > 0) {
          // put race split info
          data.put(currentRaceNr, list);
        }
        currentRaceNr = row.getRaceNr();
        list = new ArrayList<SplitTime>();
        controlNoCounter = new HashMap<>();
      }
      LegTime time = new LegTime();
      if (CompareUtility.equals(lastSplitStatus, ControlStatusCodeType.OkCode.ID)) {
        time.setLegTime(row.getLegTime());
        time.setLegTimeRaw(row.getLegTimeRaw());
      }

      OverallTime overall = new OverallTime();
      overall.setOverallTime(row.getRelativeTime());
      overall.setOverallTimeRaw(row.getOverallTimeRaw());

      // count how many times control no was used
      Long counter = controlNoCounter.get(row.getControlNo());
      if (counter == null) {
        counter = 0L;
      }
      counter++;
      controlNoCounter.put(row.getControlNo(), counter);
      Control controlNo = new Control(row.getControlNo(), counter, row.getStatusUid(), row.getTypeUid(), row.getCourseControlNr());

      if (legAnalysis.get(controlNo) == null) {
        legAnalysis.put(controlNo, new ArrayList<LegTime>());
      }
      legAnalysis.get(controlNo).add(time);
      if (overallAnalysis.get(controlNo) == null) {
        overallAnalysis.put(controlNo, new ArrayList<OverallTime>());
      }
      overallAnalysis.get(controlNo).add(overall);

      SplitTime split = new SplitTime(controlNo, time, overall, list.size());
      list.add(split);

      lastSplitStatus = row.getStatusUid();
    }
    // put last race split info
    if (splits.size() > 0) {
      data.put(currentRaceNr, list);
    }

    // realign split times by first runner
    if (winnerRaceNr != null && data.get(winnerRaceNr) != null) {
      // first runner
      List<SplitTime> winnerOrder = new ArrayList<>(data.get(winnerRaceNr));
      for (List<SplitTime> times : data.values()) {
        // build lookup
        Map<String, List<SplitTime>> timeLookup = new HashMap<>();
        for (SplitTime time : times) {
          if (timeLookup.get(time.getControl().getControlNo()) == null) {
            timeLookup.put(time.getControl().getControlNo(), new ArrayList<SplitTime>());
          }
          List<SplitTime> splitTimes = timeLookup.get(time.getControl().getControlNo());
          splitTimes.add(time);
        }
        times.clear();

        // build new time list aligned to winner
        for (SplitTime time : winnerOrder) {
          List<SplitTime> times2 = timeLookup.get(time.getControl().getControlNo());
          if (times2 != null && times2.size() > 0) {
            SplitTime t = times2.remove(0);
            times.add(t);
          }
        }
        for (List<SplitTime> splitTime : timeLookup.values()) {
          times.addAll(splitTime);
        }
      }
    }

    // update overall times
    for (List<SplitTime> splitList : data.values()) {
      Long overallTime = 0L;
      int k = 0;
      for (SplitTime time : splitList) {
        Long legTime = time.getLegTime().getLegTimeRaw();
        if (CompareUtility.equals(time.getOriginalSortCode(), k) && time.getOverallTime().getOverallTimeRaw() != null) {
          overallTime = time.getOverallTime().getOverallTimeRaw();
        }
        else {
          if (legTime != null && overallTime != null) {
            overallTime = overallTime + legTime;
            time.getOverallTime().setOverallTimeRaw(overallTime);
            time.getOverallTime().setOverallTime(FMilaUtility.formatTime(overallTime, timePrecisionUid));
          }
        }
        k++;
      }
    }

    // analyze leg data
    for (List<LegTime> legTimes : legAnalysis.values()) {
      analyzeLegTimes(legTimes, timePrecisionUid);
    }

    // analyze overall data
    for (List<OverallTime> overallTimes : overallAnalysis.values()) {
      analyzeOverallTimes(overallTimes);
    }

    return data;
  }

  private static void analyzeLegTimes(List<LegTime> legTimes, Long timePrecisionUid) {
    Long minLegTime = null;
    Collections.sort(legTimes);
    long rank = 1;
    long lastRank = 1;
    Long lastLegTimeRaw = null;
    for (LegTime legTime : legTimes) {
      if (legTime.getLegTimeRaw() != null) {
        // rank
        if (CompareUtility.equals(lastLegTimeRaw, legTime.getLegTimeRaw())) {
          legTime.setLegRank(lastRank);
        }
        else {
          lastRank = rank;
          legTime.setLegRank(rank);
        }
        rank++;
        // min
        if (minLegTime == null) {
          minLegTime = legTime.getLegTimeRaw();
        }
        else {
          minLegTime = Math.min(minLegTime, legTime.getLegTimeRaw());
        }
      }
      lastLegTimeRaw = legTime.getLegTimeRaw();
    }
    if (minLegTime != null) {
      for (LegTime legTime : legTimes) {
        if (legTime.getLegTimeRaw() != null) {
          Long diff = FMilaUtility.roundTime(legTime.getLegTimeRaw(), timePrecisionUid) - FMilaUtility.roundTime(minLegTime, timePrecisionUid);
          legTime.setTimeBehind(diff);
        }
      }
    }
  }

  private static void analyzeOverallTimes(List<OverallTime> overallTimes) {
    Collections.sort(overallTimes);
    long rank = 1;
    long lastRank = 1;
    Long lastLegTimeRaw = null;
    for (OverallTime legTime : overallTimes) {
      if (legTime.getOverallTimeRaw() != null) {
        if (CompareUtility.equals(lastLegTimeRaw, legTime.getOverallTimeRaw())) {
          legTime.setOverallRank(lastRank);
        }
        else {
          lastRank = rank;
          legTime.setOverallRank(rank);
        }
        rank++;
      }
      lastLegTimeRaw = legTime.getOverallTimeRaw();
    }
  }

}
