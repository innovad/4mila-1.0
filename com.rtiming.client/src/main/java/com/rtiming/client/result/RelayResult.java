package com.rtiming.client.result;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.client.result.split.LegResult;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.race.RaceStatusCodeType;

public class RelayResult implements Comparable<RelayResult> {

  private final Long entryNr;
  private final Long precisionUid;
  private final HashMap<Long, LegResult> legMap;

  private Long statusUid;
  private Long rank;
  private String teamName;

  private Long summaryTime;
  private String formattedSummaryTime;
  private Long timeBehind;
  private String formattedTimeBehind;
  private Double percentBehind;

  public RelayResult(Long entryNr, Long precisionUid) {
    super();
    this.entryNr = entryNr;
    this.precisionUid = precisionUid;
    legMap = new LinkedHashMap<Long, LegResult>(); // LinkedHashMap does preserve leg order
  }

  public Long getSummaryTime() {
    return summaryTime;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public String getFormattedSummaryTime() {
    return formattedSummaryTime;
  }

  public Long getRank() {
    return rank;
  }

  public void setRank(Long rank) {
    this.rank = rank;
  }

  public Long getTimeBehind() {
    return timeBehind;
  }

  public String getFormattedTimeBehind() {
    return formattedTimeBehind;
  }

  public Double getPercentBehind() {
    return percentBehind;
  }

  public void addLegResult(Long legUid, Object[] resultRow, Long legTime, Long legStatus) {
    LegResult leg = new LegResult();
    legTime = FMilaUtility.roundTime(legTime, precisionUid);
    leg.setResultRow(resultRow, legTime, legStatus);
    leg.setLegUid(legUid);
    legMap.put(legUid, leg);
  }

  public Collection<LegResult> getLegs() {
    return legMap.values();
  }

  public void calculateTimeBehind(Long winningTime, HashMap<Long, Long> winningLegTimes) throws ProcessingException {
    if (winningTime != null && getSummaryTime() != null) {
      timeBehind = getSummaryTime() - winningTime;
      percentBehind = 100 * (Double.valueOf(timeBehind) / Double.valueOf(winningTime));
      formattedTimeBehind = FMilaUtility.formatTime(timeBehind, precisionUid);
    }
    for (LegResult leg : getLegs()) {
      Long winningLegTime = winningLegTimes.get(leg.getLegUid());
      if (winningLegTime != null && leg.getSummaryTime() != null) {
        leg.setTimeBehind(leg.getSummaryTime() - winningLegTime);
        leg.setPercentBehind(100 * (Double.valueOf(leg.getTimeBehind()) / Double.valueOf(winningLegTime)));
        leg.setFormattedTimeBehind(FMilaUtility.formatTime(leg.getTimeBehind(), precisionUid));
      }
    }

  }

  public void calculate(List<Long> requiredLegUids) throws ProcessingException {
    // leg time and status
    summaryTime = 0L;
    statusUid = RaceStatusCodeType.OkCode.ID;

    // loop over required legs
    for (Long legUid : requiredLegUids) {
      LegResult leg = legMap.get(legUid);
      if (leg == null) {
        summaryTime = null;
        statusUid = RaceStatusCodeType.DidNotStartCode.ID;
      }
      else {
        if (CompareUtility.equals(statusUid, RaceStatusCodeType.OkCode.ID) &&
            CompareUtility.notEquals(leg.getLegStatus(), RaceStatusCodeType.OkCode.ID)) {
          statusUid = leg.getLegStatus(); // set the first not OK status
        }
        if (leg.getLegTime() != null && summaryTime != null) {
          summaryTime += leg.getLegTime();
        }
        else {
          summaryTime = null;
        }
        leg.setSummaryTime(summaryTime);
        if (summaryTime != null) {
          leg.setFormattedSummaryTime(FMilaUtility.formatTime(summaryTime, precisionUid));
        }
        else {
          leg.setFormattedSummaryTime(FMilaUtility.getCodeText(RaceStatusCodeType.class, leg.getLegStatus()));
        }
      }
    }
    // format overall relay summary time
    if (summaryTime != null) {
      formattedSummaryTime = FMilaUtility.formatTime(summaryTime, precisionUid);
    }
    else {
      formattedSummaryTime = FMilaUtility.getCodeText(RaceStatusCodeType.class, statusUid);
    }
  }

  @Override
  public int compareTo(RelayResult other) {
    if (other == null) {
      return -1;
    }
    if (CompareUtility.notEquals(other.getStatusUid(), getStatusUid())) {
      if (CompareUtility.equals(getStatusUid(), RaceStatusCodeType.OkCode.ID)) {
        return -1;
      }
      return 1;
    }
    if (getSummaryTime() == null) {
      return 1;
    }
    if (other.getSummaryTime() == null) {
      return -1;
    }
    return getSummaryTime().compareTo(other.getSummaryTime());
  }

  @Override
  public String toString() {
    return "[" + "leg count=" + legMap.size() + ", status=" + getStatusUid() + ", time=" + getSummaryTime() + "]";
  }

}
