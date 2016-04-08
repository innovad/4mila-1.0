package com.rtiming.server.race;

public class RaceSettings {

  private boolean usingStartlist;
  private boolean summaryTimeIsMaxTime;
  private boolean oneRaceClassTypeUid;
  private Long timePrecisionUid;
  private Long courseNr;
  private Long courseGenerationTypeUid;
  private Long eventNr;

  public boolean isUsingStartlist() {
    return usingStartlist;
  }

  public void setUsingStartlist(boolean isUsingStartlist) {
    this.usingStartlist = isUsingStartlist;
  }

  public boolean isSummaryTimeIsMaxTime() {
    return summaryTimeIsMaxTime;
  }

  public void setSummaryTimeIsMaxTime(boolean summaryTimeIsMaxTime) {
    this.summaryTimeIsMaxTime = summaryTimeIsMaxTime;
  }

  public boolean isOneRaceClassTypeUid() {
    return oneRaceClassTypeUid;
  }

  public void setOneRaceClassTypeUid(boolean isOneRaceClassTypeUid) {
    this.oneRaceClassTypeUid = isOneRaceClassTypeUid;
  }

  public Long getTimePrecisionUid() {
    return timePrecisionUid;
  }

  public void setTimePrecisionUid(Long timePrecisionUid) {
    this.timePrecisionUid = timePrecisionUid;
  }

  public Long getCourseNr() {
    return courseNr;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public Long getCourseGenerationTypeUid() {
    return courseGenerationTypeUid;
  }

  public void setCourseGenerationTypeUid(Long courseGenerationTypeUid) {
    this.courseGenerationTypeUid = courseGenerationTypeUid;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

}
