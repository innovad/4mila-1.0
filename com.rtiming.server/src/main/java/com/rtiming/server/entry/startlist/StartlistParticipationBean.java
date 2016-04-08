package com.rtiming.server.entry.startlist;

public class StartlistParticipationBean {
  private Long entryNr;
  private Long eventNr;
  private Long registrationNr;
  private Long startlistSettingNr;

  private Long nationUid;
  private Long clubNr;
  private Long startTime;
  private Long bibNo;
  private Long startTimeWish;
  private Long registrationStartlistSettingOptionUid;
  private boolean isVacant = false;

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public Long getNationUid() {
    return nationUid;
  }

  public void setNationUid(Long nationUid) {
    this.nationUid = nationUid;
  }

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getBibNo() {
    return bibNo;
  }

  public void setBibNo(Long bibNo) {
    this.bibNo = bibNo;
  }

  public Long getStartTimeWish() {
    return startTimeWish;
  }

  public void setStartTimeWish(Long startTimeWish) {
    this.startTimeWish = startTimeWish;
  }

  public Long getRegistrationStartlistSettingOptionUid() {
    return registrationStartlistSettingOptionUid;
  }

  public void setRegistrationStartlistSettingOptionUid(Long registrationStartlistSettingOptionUid) {
    this.registrationStartlistSettingOptionUid = registrationStartlistSettingOptionUid;
  }

  public void setVacant(boolean isVacantParticipation) {
    this.isVacant = isVacantParticipation;
  }

  public boolean isVacant() {
    return isVacant;
  }

  public Long getStartlistSettingNr() {
    return startlistSettingNr;
  }

  public void setStartlistSettingNr(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
  }

  @Override
  public String toString() {
    return "ParticipationBean [entryNr=" + entryNr + ", eventNr=" + eventNr + ", registrationNr=" + registrationNr + ", startlistSettingNr=" + startlistSettingNr + ", nationUid=" + nationUid + ", clubNr=" + clubNr + ", startTime=" + startTime + ", bibNo=" + bibNo + ", startTimeWish=" + startTimeWish + ", registrationStartlistSettingOptionUid=" + registrationStartlistSettingOptionUid + ", isVacant=" + isVacant + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((entryNr == null) ? 0 : entryNr.hashCode());
    result = prime * result + ((eventNr == null) ? 0 : eventNr.hashCode());
    result = prime * result + (isVacant ? 1231 : 1237);
    result = prime * result + ((startlistSettingNr == null) ? 0 : startlistSettingNr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StartlistParticipationBean other = (StartlistParticipationBean) obj;
    if (entryNr == null) {
      if (other.entryNr != null) {
        return false;
      }
    }
    else if (!entryNr.equals(other.entryNr)) {
      return false;
    }
    if (eventNr == null) {
      if (other.eventNr != null) {
        return false;
      }
    }
    else if (!eventNr.equals(other.eventNr)) {
      return false;
    }
    if (isVacant != other.isVacant) {
      return false;
    }
    if (startlistSettingNr == null) {
      if (other.startlistSettingNr != null) {
        return false;
      }
    }
    else if (!startlistSettingNr.equals(other.startlistSettingNr)) {
      return false;
    }
    return true;
  }

}
