package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.List;

public class RegistrationBean {

  private final Long registrationNr;
  private final Long startlistSettingOptionUid;
  private final List<StartlistParticipationBean> participations;

  public RegistrationBean(Long registrationNr, Long startlistSettingOptionUid) {
    super();
    this.registrationNr = registrationNr;
    this.startlistSettingOptionUid = startlistSettingOptionUid;
    this.participations = new ArrayList<StartlistParticipationBean>();
  }

  public void addParticipation(StartlistParticipationBean bean) {
    participations.add(bean);
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public Long getStartlistSettingOptionUid() {
    return startlistSettingOptionUid;
  }

  public List<StartlistParticipationBean> getParticipations() {
    return participations;
  }

  @Override
  public String toString() {
    return "RegistrationBean [registrationNr=" + registrationNr + ", startlistSettingOptionUid=" + startlistSettingOptionUid + ", participations=" + participations + "]";
  }

}
