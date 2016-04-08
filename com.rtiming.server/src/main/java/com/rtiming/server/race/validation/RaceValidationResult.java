package com.rtiming.server.race.validation;

import java.util.List;

import com.rtiming.server.race.RaceControlBean;

public class RaceValidationResult {

  private final long statusUid;
  private final long ignoredLegsTimeSum;
  private final Long legTime;
  private final Long startTime;
  private final List<RaceControlBean> controls;

  public RaceValidationResult(List<RaceControlBean> controls, Long legTime, Long startTime, long statusUid, long ignoredLegsTimeSum) {
    super();
    this.controls = controls;
    this.legTime = legTime;
    this.startTime = startTime;
    this.statusUid = statusUid;
    this.ignoredLegsTimeSum = ignoredLegsTimeSum;
  }

  public long getIgnoredLegsTimeSum() {
    return ignoredLegsTimeSum;
  }

  public Long getStartTime() {
    return startTime;
  }

  public long getStatusUid() {
    return statusUid;
  }

  public Long getLegTime() {
    return legTime;
  }

  public List<RaceControlBean> getControls() {
    return controls;
  }

  @Override
  public String toString() {
    return "RaceValidationResult [statusUid=" + statusUid + ", ignoredLegsTimeSum=" + ignoredLegsTimeSum + "]";
  }

}
