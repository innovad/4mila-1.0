package com.rtiming.shared.entry.startlist;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class StartlistSettingRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long startlistSettingNr;
  private Long eventNr;
  private Long bibNoOrderUid;
  private Long clazzUid;
  private Long clazzTypeUid;
  private String course;
  private Long courseNr;
  private Long typeUid;
  private Date firstStart;
  private Date lastStart;
  private Long participationCount;
  private Long interval;
  private Long bibNoFrom;
  private Long vacant;
  private String sameClasses;

  public Long getStartlistSettingNr() {
    return startlistSettingNr;
  }

  public void setStartlistSettingNr(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
  }

  public Long getBibNoOrderUid() {
    return bibNoOrderUid;
  }

  public void setBibNoOrderUid(Long bibNoOrderUid) {
    this.bibNoOrderUid = bibNoOrderUid;
  }

  public Long getClazzUid() {
    return clazzUid;
  }

  public void setClazzUid(Long clazzUid) {
    this.clazzUid = clazzUid;
  }

  public Long getClazzTypeUid() {
    return clazzTypeUid;
  }

  public void setClazzTypeUid(Long clazzTypeUid) {
    this.clazzTypeUid = clazzTypeUid;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public Long getCourseNr() {
    return courseNr;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Date getFirstStart() {
    return firstStart;
  }

  public void setFirstStart(Date firstStart) {
    this.firstStart = firstStart;
  }

  public Date getLastStart() {
    return lastStart;
  }

  public void setLastStart(Date lastStart) {
    this.lastStart = lastStart;
  }

  public Long getParticipationCount() {
    return participationCount;
  }

  public void setParticipationCount(Long participationCount) {
    this.participationCount = participationCount;
  }

  public Long getInterval() {
    return interval;
  }

  public void setInterval(Long interval) {
    this.interval = interval;
  }

  public Long getBibNoFrom() {
    return bibNoFrom;
  }

  public void setBibNoFrom(Long bibNoFrom) {
    this.bibNoFrom = bibNoFrom;
  }

  public Long getVacant() {
    return vacant;
  }

  public void setVacant(Long vacant) {
    this.vacant = vacant;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public String getSameClasses() {
    return sameClasses;
  }

  public void setSameClasses(String sameClasses) {
    this.sameClasses = sameClasses;
  }

}
