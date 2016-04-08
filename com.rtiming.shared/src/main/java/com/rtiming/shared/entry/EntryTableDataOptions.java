package com.rtiming.shared.entry;

import java.io.Serializable;


public class EntryTableDataOptions implements Serializable {

  private static final long serialVersionUID = 1L;
  private EntryList presentationType;
  private Long clientNr;
  private Long registrationNr;
  private Long classUid;
  private Long courseNr;
  private Long clubNr;

  public EntryList getPresentationType() {
    return presentationType;
  }

  public void setPresentationType(EntryList presentationType) {
    this.presentationType = presentationType;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public Long getClassUid() {
    return classUid;
  }

  public void setClassUid(Long classUid) {
    this.classUid = classUid;
  }

  public Long getCourseNr() {
    return courseNr;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

}
