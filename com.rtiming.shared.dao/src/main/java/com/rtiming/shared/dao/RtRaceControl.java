package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_race_control database table.
 */
@Entity
@Table(name = "rt_race_control")
@UploadConfiguration(uploadOrder = 540)
public class RtRaceControl implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRaceControlKey id;

  @Column(name = "leg_time")
  private Long legTime;

  @Column(name = "manual_status")
  private Boolean manualStatus;

  @Column(name = "overall_time")
  private Long overallTime;

  @Column(name = "shift_time")
  private Long shiftTime;

  private Long sortcode;

  @Column(name = "status_uid")
  private Long statusUid;

  @Column(name = "race_nr")
  private Long raceNr;

  @Column(name = "course_control_nr")
  private Long courseControlNr;

  @Column(name = "control_nr")
  private Long controlNr;

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long getRaceNr() {
    return raceNr;
  }

  //bi-directional many-to-one association to RtControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "control_nr", referencedColumnName = "control_nr", insertable = false, updatable = false)
  })
  private RtControl rtControl;

  //bi-directional many-to-one association to RtCourseControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "course_control_nr", referencedColumnName = "course_control_nr", insertable = false, updatable = false)
  })
  private RtCourseControl rtCourseControl;

  //bi-directional many-to-one association to RtRace
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "race_nr", referencedColumnName = "race_nr", insertable = false, updatable = false)
  })
  private RtRace rtRace;

  public RtRaceControl() {
  }

  public void setId(RtRaceControlKey id) {
    this.id = id;
  }

  public RtRaceControlKey getId() {
    return id;
  }

  public Long getLegTime() {
    return this.legTime;
  }

  public void setLegTime(Long legTime) {
    this.legTime = legTime;
  }

  public Boolean getManualStatus() {
    return this.manualStatus;
  }

  public void setManualStatus(Boolean manualStatus) {
    this.manualStatus = manualStatus;
  }

  public Long getOverallTime() {
    return this.overallTime;
  }

  public void setOverallTime(Long overallTime) {
    this.overallTime = overallTime;
  }

  public Long getShiftTime() {
    return this.shiftTime;
  }

  public void setShiftTime(Long shiftTime) {
    this.shiftTime = shiftTime;
  }

  public Long getSortcode() {
    return this.sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public Long getStatusUid() {
    return this.statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public RtControl getRtControl() {
    return this.rtControl;
  }

  public void setRtControl(RtControl rtControl) {
    this.rtControl = rtControl;
  }

  public RtRace getRtRace() {
    return this.rtRace;
  }

  public void setRtRace(RtRace rtRace) {
    this.rtRace = rtRace;
  }

}
