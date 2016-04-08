package com.rtiming.server.entry.startlist;

import java.util.LinkedList;

import com.rtiming.shared.entry.startlist.StartlistSettingFormData;

public class StartlistSettingBean {

  private final Long startlistSettingNr;
  private final Long eventNr;
  private final StartlistSettingFormData settings;
  private final LinkedList<StartlistParticipationBean> list;

  private Long firstStart;
  private Long lastStart;

  public StartlistSettingBean(Long startlistSettingNr, Long eventNr, StartlistSettingFormData settings, LinkedList<StartlistParticipationBean> list) {
    super();
    this.startlistSettingNr = startlistSettingNr;
    this.eventNr = eventNr;
    this.settings = settings;
    this.list = list;
  }

  public Long getStartlistSettingNr() {
    return startlistSettingNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public StartlistSettingFormData getSettings() {
    return settings;
  }

  public LinkedList<StartlistParticipationBean> getList() {
    return list;
  }

  public Long getFirstStart() {
    return firstStart;
  }

  public void setFirstStart(Long firstStart) {
    this.firstStart = firstStart;
  }

  public Long getLastStart() {
    return lastStart;
  }

  public void setLastStart(Long lastStart) {
    this.lastStart = lastStart;
  }

}
