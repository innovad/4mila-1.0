package com.rtiming.client.common.report.jrxml;

import java.util.List;

/**
 * 
 */
public class ReportParam {

  private Long eventNr;
  private List<Long> classUids;
  private List<Long> courseNrs;
  private List<Long> clubNrs;

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public List<Long> getClassUids() {
    return classUids;
  }

  public void setClassUids(List<Long> classUids) {
    this.classUids = classUids;
  }

  public List<Long> getCourseNrs() {
    return courseNrs;
  }

  public void setCourseNrs(List<Long> courseNrs) {
    this.courseNrs = courseNrs;
  }

  public List<Long> getClubNrs() {
    return clubNrs;
  }

  public void setClubNrs(List<Long> clubNrs) {
    this.clubNrs = clubNrs;
  }

}
