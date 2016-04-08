package com.rtiming.shared.common.database.sql;

import java.io.Serializable;
import java.util.Date;

public class EventBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long eventNr;
  private Long clientNr;
  private Date evtLastUpload;
  private String name;
  private Long typeUid;
  private String location;
  private String map;
  private Date evtZero;
  private Date evtFinish;
  private Integer timezone;
  private Long punchingSystemUid;
  private byte[] logoData;
  private String format;

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

  public Date getEvtLastUpload() {
    return evtLastUpload;
  }

  public void setEvtLastUpload(Date evtLastUpload) {
    this.evtLastUpload = evtLastUpload;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
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

  public Integer getTimezone() {
    return timezone;
  }

  public void setTimezone(Integer timezone) {
    this.timezone = timezone;
  }

  public Long getPunchingSystemUid() {
    return punchingSystemUid;
  }

  public void setPunchingSystemUid(Long punchingSystemUid) {
    this.punchingSystemUid = punchingSystemUid;
  }

  public byte[] getLogoData() {
    return logoData;
  }

  public void setLogoData(byte[] logoData) {
    this.logoData = logoData;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

}
