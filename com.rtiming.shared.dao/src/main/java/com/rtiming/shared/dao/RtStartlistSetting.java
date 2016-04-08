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

/**
 * The persistent class for the rt_startlist_setting database table.
 */
@Entity
@Table(name = "rt_startlist_setting")
public class RtStartlistSetting implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtStartlistSettingKey id;

  @Column(name = "bib_no_from")
  private Long bibNoFrom;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "bib_no_order_uid")
  private Long bibNoOrderUid;

  @Column(name = "first_starttime")
  private Long firstStarttime;

  @Column(name = "group_registrations")
  private Boolean groupRegistrations;

  @Column(name = "start_interval")
  private Long startInterval;

  @Column(name = "type_uid")
  private Long typeUid;

  @Column(name = "vacant_absolute")
  private Long vacantAbsolute;

  @Column(name = "vacant_percent")
  private Long vacantPercent;

  @Column(name = "vacant_position_uid")
  private Long vacantPositionUid;

  //bi-directional many-to-one association to RtCourse
  @OneToMany(mappedBy = "rtStartlistSetting")
  private List<RtCourse> rtCourses;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtStartlistSetting")
  private List<RtEventClass> rtEventClasses;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtStartlistSettingOption
  @OneToMany(mappedBy = "rtStartlistSetting")
  private List<RtStartlistSettingOption> rtStartlistSettingOptions;

  //bi-directional many-to-one association to RtStartlistSettingVacant
  @OneToMany(mappedBy = "rtStartlistSetting")
  private List<RtStartlistSettingVacant> rtStartlistSettingVacants;

  public RtStartlistSetting() {
  }

  public RtStartlistSettingKey getId() {
    return this.id;
  }

  public void setId(RtStartlistSettingKey id) {
    this.id = id;
  }

  public Long getBibNoFrom() {
    return this.bibNoFrom;
  }

  public void setBibNoFrom(Long bibNoFrom) {
    this.bibNoFrom = bibNoFrom;
  }

  public Long getBibNoOrderUid() {
    return this.bibNoOrderUid;
  }

  public void setBibNoOrderUid(Long bibNoOrderUid) {
    this.bibNoOrderUid = bibNoOrderUid;
  }

  public Long getFirstStarttime() {
    return this.firstStarttime;
  }

  public void setFirstStarttime(Long firstStarttime) {
    this.firstStarttime = firstStarttime;
  }

  public Boolean getGroupRegistrations() {
    return this.groupRegistrations;
  }

  public void setGroupRegistrations(Boolean groupRegistrations) {
    this.groupRegistrations = groupRegistrations;
  }

  public Long getStartInterval() {
    return this.startInterval;
  }

  public void setStartInterval(Long startInterval) {
    this.startInterval = startInterval;
  }

  public Long getTypeUid() {
    return this.typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Long getVacantAbsolute() {
    return this.vacantAbsolute;
  }

  public void setVacantAbsolute(Long vacantAbsolute) {
    this.vacantAbsolute = vacantAbsolute;
  }

  public Long getVacantPercent() {
    return this.vacantPercent;
  }

  public void setVacantPercent(Long vacantPercent) {
    this.vacantPercent = vacantPercent;
  }

  public Long getVacantPositionUid() {
    return this.vacantPositionUid;
  }

  public void setVacantPositionUid(Long vacantPositionUid) {
    this.vacantPositionUid = vacantPositionUid;
  }

  public List<RtCourse> getRtCourses() {
    return this.rtCourses;
  }

  public void setRtCourses(List<RtCourse> rtCourses) {
    this.rtCourses = rtCourses;
  }

  public RtCourse addRtCours(RtCourse rtCours) {
    getRtCourses().add(rtCours);
    rtCours.setRtStartlistSetting(this);

    return rtCours;
  }

  public RtCourse removeRtCours(RtCourse rtCours) {
    getRtCourses().remove(rtCours);
    rtCours.setRtStartlistSetting(null);

    return rtCours;
  }

  public List<RtEventClass> getRtEventClasses() {
    return this.rtEventClasses;
  }

  public void setRtEventClasses(List<RtEventClass> rtEventClasses) {
    this.rtEventClasses = rtEventClasses;
  }

  public RtEventClass addRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().add(rtEventClass);
    rtEventClass.setRtStartlistSetting(this);

    return rtEventClass;
  }

  public RtEventClass removeRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().remove(rtEventClass);
    rtEventClass.setRtStartlistSetting(null);

    return rtEventClass;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtEvent getRtEvent() {
    return this.rtEvent;
  }

  public void setRtEvent(RtEvent rtEvent) {
    this.rtEvent = rtEvent;
  }

  public List<RtStartlistSettingOption> getRtStartlistSettingOptions() {
    return this.rtStartlistSettingOptions;
  }

  public void setRtStartlistSettingOptions(List<RtStartlistSettingOption> rtStartlistSettingOptions) {
    this.rtStartlistSettingOptions = rtStartlistSettingOptions;
  }

  public RtStartlistSettingOption addRtStartlistSettingOption(RtStartlistSettingOption rtStartlistSettingOption) {
    getRtStartlistSettingOptions().add(rtStartlistSettingOption);
    rtStartlistSettingOption.setRtStartlistSetting(this);

    return rtStartlistSettingOption;
  }

  public RtStartlistSettingOption removeRtStartlistSettingOption(RtStartlistSettingOption rtStartlistSettingOption) {
    getRtStartlistSettingOptions().remove(rtStartlistSettingOption);
    rtStartlistSettingOption.setRtStartlistSetting(null);

    return rtStartlistSettingOption;
  }

  public List<RtStartlistSettingVacant> getRtStartlistSettingVacants() {
    return this.rtStartlistSettingVacants;
  }

  public void setRtStartlistSettingVacants(List<RtStartlistSettingVacant> rtStartlistSettingVacants) {
    this.rtStartlistSettingVacants = rtStartlistSettingVacants;
  }

  public RtStartlistSettingVacant addRtStartlistSettingVacant(RtStartlistSettingVacant rtStartlistSettingVacant) {
    getRtStartlistSettingVacants().add(rtStartlistSettingVacant);
    rtStartlistSettingVacant.setRtStartlistSetting(this);

    return rtStartlistSettingVacant;
  }

  public RtStartlistSettingVacant removeRtStartlistSettingVacant(RtStartlistSettingVacant rtStartlistSettingVacant) {
    getRtStartlistSettingVacants().remove(rtStartlistSettingVacant);
    rtStartlistSettingVacant.setRtStartlistSetting(null);

    return rtStartlistSettingVacant;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

}
