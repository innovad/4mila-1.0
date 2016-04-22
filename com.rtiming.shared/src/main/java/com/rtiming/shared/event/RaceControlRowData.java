package com.rtiming.shared.event;

import java.io.Serializable;
import java.util.Date;

public class RaceControlRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long raceNr;
  private Long raceControlNr;
  private Long courseControlNr;
  private Long typeUid;
  private String controlNo;
  private Long sortCode;
  private Long statusUid;
  private Date overallTime;
  private Long overallTimeRaw;
  private String relativeTime;
  private String legTime;
  private Long legTimeRaw;
  private boolean countLeg;
  private boolean manualStatus;
  private Long shiftTime;

  public Long getRaceNr() {
    return raceNr;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long getRaceControlNr() {
    return raceControlNr;
  }

  public void setRaceControlNr(Long raceControlNr) {
    this.raceControlNr = raceControlNr;
  }

  public Long getCourseControlNr() {
    return courseControlNr;
  }

  public void setCourseControlNr(Long courseControlNr) {
    this.courseControlNr = courseControlNr;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public String getControlNo() {
    return controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public Long getSortCode() {
    return sortCode;
  }

  public void setSortCode(Long sortCode) {
    this.sortCode = sortCode;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public Date getOverallTime() {
    return overallTime;
  }

  public void setOverallTime(Date time) {
    this.overallTime = time;
  }

  public Long getOverallTimeRaw() {
    return overallTimeRaw;
  }

  public void setOverallTimeRaw(Long overallTimeRaw) {
    this.overallTimeRaw = overallTimeRaw;
  }

  public String getRelativeTime() {
    return relativeTime;
  }

  public void setRelativeTime(String overallTime) {
    this.relativeTime = overallTime;
  }

  public String getLegTime() {
    return legTime;
  }

  public void setLegTime(String legTime) {
    this.legTime = legTime;
  }

  public Long getLegTimeRaw() {
    return legTimeRaw;
  }

  public void setLegTimeRaw(Long legTimeRaw) {
    this.legTimeRaw = legTimeRaw;
  }

  public boolean isCountLeg() {
    return countLeg;
  }

  public void setCountLeg(boolean countLeg) {
    this.countLeg = countLeg;
  }

  public boolean isManualStatus() {
    return manualStatus;
  }

  public void setManualStatus(boolean manualStatus) {
    this.manualStatus = manualStatus;
  }

  public Long getShiftTime() {
    return shiftTime;
  }

  public void setShiftTime(Long shiftTime) {
    this.shiftTime = shiftTime;
  }

}
