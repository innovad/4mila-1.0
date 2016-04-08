package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_control database table.
 */
@Entity
@Table(name = "rt_control")
@UploadConfiguration(uploadOrder = 200)
public class RtControl implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtControlKey id;

  private Boolean active;

  @Column(name = "control_no")
  private String controlNo;

  private Double globalx;

  private Double globaly;

  private Double localx;

  private Double localy;

  @Column(name = "type_uid")
  private Long typeUid;

  @Column(name = "event_nr")
  private Long eventNr;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtControlReplacement
  @OneToMany(mappedBy = "rtControl1")
  private List<RtControlReplacement> rtControlReplacements1;

  //bi-directional many-to-one association to RtControlReplacement
  @OneToMany(mappedBy = "rtControl2")
  private List<RtControlReplacement> rtControlReplacements2;

  //bi-directional many-to-one association to RtCourseControl
  @OneToMany(mappedBy = "rtControl")
  private List<RtCourseControl> rtCourseControls;

  //bi-directional many-to-one association to RtRaceControl
  @OneToMany(mappedBy = "rtControl")
  private List<RtRaceControl> rtRaceControls;

  public RtControl() {
  }

  public RtControlKey getId() {
    return id;
  }

  public void setId(RtControlKey id) {
    this.id = id;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getControlNo() {
    return this.controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public Double getGlobalx() {
    return this.globalx;
  }

  public void setGlobalx(Double globalx) {
    this.globalx = globalx;
  }

  public Double getGlobaly() {
    return this.globaly;
  }

  public void setGlobaly(Double globaly) {
    this.globaly = globaly;
  }

  public Double getLocalx() {
    return this.localx;
  }

  public void setLocalx(Double localx) {
    this.localx = localx;
  }

  public Double getLocaly() {
    return this.localy;
  }

  public void setLocaly(Double localy) {
    this.localy = localy;
  }

  public Long getTypeUid() {
    return this.typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public RtEvent getRtEvent() {
    return this.rtEvent;
  }

  public void setRtEvent(RtEvent rtEvent) {
    this.rtEvent = rtEvent;
  }

  public List<RtControlReplacement> getRtControlReplacements1() {
    return this.rtControlReplacements1;
  }

  public void setRtControlReplacements1(List<RtControlReplacement> rtControlReplacements1) {
    this.rtControlReplacements1 = rtControlReplacements1;
  }

  public RtControlReplacement addRtControlReplacements1(RtControlReplacement controlReplacement) {
    getRtControlReplacements1().add(controlReplacement);
    controlReplacement.setRtControl1(this);

    return controlReplacement;
  }

  public RtControlReplacement removeRtControlReplacements1(RtControlReplacement controlReplacement) {
    getRtControlReplacements1().remove(controlReplacement);
    controlReplacement.setRtControl1(null);

    return controlReplacement;
  }

  public List<RtControlReplacement> getRtControlReplacements2() {
    return this.rtControlReplacements2;
  }

  public void setRtControlReplacements2(List<RtControlReplacement> rtControlReplacements2) {
    this.rtControlReplacements2 = rtControlReplacements2;
  }

  public RtControlReplacement addRtControlReplacements2(RtControlReplacement controlReplacement) {
    getRtControlReplacements2().add(controlReplacement);
    controlReplacement.setRtControl2(this);

    return controlReplacement;
  }

  public RtControlReplacement removeRtControlReplacements2(RtControlReplacement controlReplacement) {
    getRtControlReplacements2().remove(controlReplacement);
    controlReplacement.setRtControl2(null);

    return controlReplacement;
  }

  public List<RtCourseControl> getRtCourseControls() {
    return this.rtCourseControls;
  }

  public void setRtCourseControls(List<RtCourseControl> rtCourseControls) {
    this.rtCourseControls = rtCourseControls;
  }

  public RtCourseControl addRtCourseControl(RtCourseControl rtCourseControl) {
    getRtCourseControls().add(rtCourseControl);
    rtCourseControl.setRtControl(this);

    return rtCourseControl;
  }

  public RtCourseControl removeRtCourseControl(RtCourseControl rtCourseControl) {
    getRtCourseControls().remove(rtCourseControl);
    rtCourseControl.setRtControl(null);

    return rtCourseControl;
  }

  public List<RtRaceControl> getRtRaceControls() {
    return this.rtRaceControls;
  }

  public void setRtRaceControls(List<RtRaceControl> rtRaceControls) {
    this.rtRaceControls = rtRaceControls;
  }

  public RtRaceControl addRtRaceControl(RtRaceControl rtRaceControl) {
    getRtRaceControls().add(rtRaceControl);
    rtRaceControl.setRtControl(this);

    return rtRaceControl;
  }

  public RtRaceControl removeRtRaceControl(RtRaceControl rtRaceControl) {
    getRtRaceControls().remove(rtRaceControl);
    rtRaceControl.setRtControl(null);

    return rtRaceControl;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

}
