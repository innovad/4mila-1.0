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
 * The persistent class for the rt_event_class database table.
 */
@Entity
@Table(name = "rt_event_class")
@UploadConfiguration(uploadOrder = 80, filteredColumns = {"startlist_setting_nr", "fee_group_nr"})
public class RtEventClass implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEventClassKey id;

  @Column(name = "course_nr")
  private Long courseNr;

  private Long sortcode;

  @Column(name = "team_size_max")
  private Long teamSizeMax;

  @Column(name = "team_size_min")
  private Long teamSizeMin;

  @Column(name = "time_precision_uid")
  private Long timePrecisionUid;

  @Column(name = "type_uid")
  private Long typeUid;

  @Column(name = "startlist_setting_nr")
  private Long startlistSettingNr;

  @Column(name = "parent_uid")
  private Long parentUid;

  @Column(name = "course_generation_type_uid")
  private Long courseGenerationTypeUid;

  @Column(name = "fee_group_nr")
  private Long feeGroupNr;

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

  //bi-directional many-to-one association to RtEventClass
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "class_uid", insertable = false, updatable = false),
      @JoinColumn(name = "parent_uid", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEventClass rtEventClass;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtEventClass")
  private List<RtEventClass> rtEventClasses;

  //bi-directional many-to-one association to RtFeeGroup
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "fee_group_nr", referencedColumnName = "fee_group_nr", insertable = false, updatable = false)
  })
  private RtFeeGroup rtFeeGroup;

  //bi-directional many-to-one association to RtStartlistSetting
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "startlist_setting_nr", referencedColumnName = "startlist_setting_nr", insertable = false, updatable = false)
  })
  private RtStartlistSetting rtStartlistSetting;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "class_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false),
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false)
  })
  private RtUc rtUc1;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "parent_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc2;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "course_nr", referencedColumnName = "course_nr", insertable = false, updatable = false)
  })
  private RtCourse rtCourse;

  //bi-directional many-to-one association to RtParticipation
  @OneToMany(mappedBy = "rtEventClass")
  private List<RtParticipation> rtParticipations;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtEventClass")
  private List<RtRace> rtRaces;

  public RtEventClass() {
  }

  public RtEventClassKey getId() {
    return this.id;
  }

  public void setId(RtEventClassKey id) {
    this.id = id;
  }

  public Long getCourseNr() {
    return this.courseNr;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public Long getSortcode() {
    return this.sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public Long getTeamSizeMax() {
    return this.teamSizeMax;
  }

  public void setTeamSizeMax(Long teamSizeMax) {
    this.teamSizeMax = teamSizeMax;
  }

  public Long getTeamSizeMin() {
    return this.teamSizeMin;
  }

  public void setTeamSizeMin(Long teamSizeMin) {
    this.teamSizeMin = teamSizeMin;
  }

  public Long getTimePrecisionUid() {
    return this.timePrecisionUid;
  }

  public void setTimePrecisionUid(Long timePrecisionUid) {
    this.timePrecisionUid = timePrecisionUid;
  }

  public Long getTypeUid() {
    return this.typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
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

  public RtEventClass getRtEventClass() {
    return this.rtEventClass;
  }

  public void setRtEventClass(RtEventClass rtEventClass) {
    this.rtEventClass = rtEventClass;
  }

  public List<RtEventClass> getRtEventClasses() {
    return this.rtEventClasses;
  }

  public void setRtEventClasses(List<RtEventClass> rtEventClasses) {
    this.rtEventClasses = rtEventClasses;
  }

  public RtEventClass addRtEventClass(RtEventClass eventClass) {
    getRtEventClasses().add(eventClass);
    eventClass.setRtEventClass(this);

    return eventClass;
  }

  public RtEventClass removeRtEventClass(RtEventClass eventClass) {
    getRtEventClasses().remove(eventClass);
    eventClass.setRtEventClass(null);

    return eventClass;
  }

  public RtFeeGroup getRtFeeGroup() {
    return this.rtFeeGroup;
  }

  public void setRtFeeGroup(RtFeeGroup rtFeeGroup) {
    this.rtFeeGroup = rtFeeGroup;
  }

  public RtStartlistSetting getRtStartlistSetting() {
    return this.rtStartlistSetting;
  }

  public void setRtStartlistSetting(RtStartlistSetting rtStartlistSetting) {
    this.rtStartlistSetting = rtStartlistSetting;
  }

  public RtUc getRtUc1() {
    return this.rtUc1;
  }

  public void setRtUc1(RtUc rtUc1) {
    this.rtUc1 = rtUc1;
  }

  public RtUc getRtUc2() {
    return this.rtUc2;
  }

  public void setRtUc2(RtUc rtUc2) {
    this.rtUc2 = rtUc2;
  }

  public List<RtParticipation> getRtParticipations() {
    return this.rtParticipations;
  }

  public void setRtParticipations(List<RtParticipation> rtParticipations) {
    this.rtParticipations = rtParticipations;
  }

  public RtParticipation addRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().add(rtParticipation);
    rtParticipation.setRtEventClass(this);

    return rtParticipation;
  }

  public RtParticipation removeRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().remove(rtParticipation);
    rtParticipation.setRtEventClass(null);

    return rtParticipation;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtEventClass(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtEventClass(null);

    return rtRace;
  }

  public void setCourseGenerationTypeUid(Long courseGenerationTypeUid) {
    this.courseGenerationTypeUid = courseGenerationTypeUid;
  }

  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

  public void setStartlistSettingNr(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
  }

}
