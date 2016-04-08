package com.rtiming.shared.event;

import java.io.Serializable;
import java.util.Date;

public class EventRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long eventNr;
  private Long clientNr;
  private Long defaultEventNr;
  private String name;
  private String location;
  private String map;
  private Long typeUid;
  private Date evtZero;
  private Date evtFinish;
  private Integer timeZone;
  private Date evtLastUpload;

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getDefaultEventNr() {
    return defaultEventNr;
  }

  public void setDefaultEventNr(Long defaultEventNr) {
    this.defaultEventNr = defaultEventNr;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Date getEvtZero() {
    return evtZero;
  }

  public void setEvtZero(Date evtZero) {
    this.evtZero = evtZero;
  }

  public Date getEvtFinish() {
    return evtFinish;
  }

  public void setEvtFinish(Date evtFinish) {
    this.evtFinish = evtFinish;
  }

  public Integer getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(Integer timeZone) {
    this.timeZone = timeZone;
  }

  public Date getEvtLastUpload() {
    return evtLastUpload;
  }

  public void setEvtLastUpload(Date evtLastUpload) {
    this.evtLastUpload = evtLastUpload;
  }

}
