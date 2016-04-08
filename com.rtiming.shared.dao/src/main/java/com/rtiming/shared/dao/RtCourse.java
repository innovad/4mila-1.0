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
 * The persistent class for the rt_course database table.
 */
@Entity
@Table(name = "rt_course")
@UploadConfiguration(uploadOrder = 220)
public class RtCourse implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtCourseKey id;

  private Long climb;

  private Long length;

  private String shortcut;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "startlist_setting_nr")
  private Long startlistSettingNr;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtStartlistSetting
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "startlist_setting_nr", referencedColumnName = "startlist_setting_nr", insertable = false, updatable = false)
  })
  private RtStartlistSetting rtStartlistSetting;

  //bi-directional many-to-one association to RtCourseControl
  @OneToMany(mappedBy = "rtCourse")
  private List<RtCourseControl> rtCourseControls;

  //bi-directional many-to-one association to RtCourseControl
  @OneToMany(mappedBy = "rtCourse")
  private List<RtEventClass> rtEventClasses;

  public RtCourse() {
  }

  public RtCourseKey getId() {
    return this.id;
  }

  public void setId(RtCourseKey id) {
    this.id = id;
  }

  public Long getClimb() {
    return this.climb;
  }

  public void setClimb(Long climb) {
    this.climb = climb;
  }

  public Long getLength() {
    return this.length;
  }

  public void setLength(Long length) {
    this.length = length;
  }

  public String getShortcut() {
    return this.shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public RtEvent getRtEvent() {
    return this.rtEvent;
  }

  public void setRtEvent(RtEvent rtEvent) {
    this.rtEvent = rtEvent;
  }

  public RtStartlistSetting getRtStartlistSetting() {
    return this.rtStartlistSetting;
  }

  public void setRtStartlistSetting(RtStartlistSetting rtStartlistSetting) {
    this.rtStartlistSetting = rtStartlistSetting;
  }

  public List<RtCourseControl> getRtCourseControls() {
    return this.rtCourseControls;
  }

  public void setRtCourseControls(List<RtCourseControl> rtCourseControls) {
    this.rtCourseControls = rtCourseControls;
  }

  public RtCourseControl addRtCourseControl(RtCourseControl rtCourseControl) {
    getRtCourseControls().add(rtCourseControl);
    rtCourseControl.setRtCourse(this);

    return rtCourseControl;
  }

  public RtCourseControl removeRtCourseControl(RtCourseControl rtCourseControl) {
    getRtCourseControls().remove(rtCourseControl);
    rtCourseControl.setRtCourse(null);

    return rtCourseControl;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public void setStartlistSettingNr(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
  }

}
