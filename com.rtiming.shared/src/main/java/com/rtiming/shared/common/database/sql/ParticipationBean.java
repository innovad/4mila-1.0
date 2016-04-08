package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

public class ParticipationBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long eventNr;
  private Long entryNr;
  private Long clientNr;
  private Long classUid;
  private Long summaryTime;
  private Long statusUid;
  private Long startTime;
  private Long startblockUid;

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getClassUid() {
    return classUid;
  }

  public void setClassUid(Long classUid) {
    this.classUid = classUid;
  }

  public Long getSummaryTime() {
    return summaryTime;
  }

  public void setSummaryTime(Long summaryTime) {
    this.summaryTime = summaryTime;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getStartblockUid() {
    return startblockUid;
  }

  public void setStartblockUid(Long startblockUid) {
    this.startblockUid = startblockUid;
  }

}
