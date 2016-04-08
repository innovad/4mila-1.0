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
 * The persistent class for the rt_participation database table.
 */
@Entity
@Table(name = "rt_participation")
@UploadConfiguration(uploadOrder = 520)
public class RtParticipation implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtParticipationKey id;

  @Column(name = "start_time")
  private Long startTime;

  @Column(name = "status_uid")
  private Long statusUid;

  @Column(name = "summary_time")
  private Long summaryTime;

  @Column(name = "startblock_uid")
  private Long startblockUid;

  @Column(name = "class_uid")
  private Long classUid;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtEntry
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "entry_nr", referencedColumnName = "entry_nr", insertable = false, updatable = false)
  })
  private RtEntry rtEntry;

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
      @JoinColumn(name = "class_uid", referencedColumnName = "class_uid", insertable = false, updatable = false),
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEventClass rtEventClass;

  //bi-directional many-to-one association to RtEventStartblock
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false),
      @JoinColumn(name = "startblock_uid", referencedColumnName = "startblock_uid", insertable = false, updatable = false)
  })
  private RtEventStartblock rtEventStartblock;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtParticipation")
  private List<RtRace> rtRaces;

  public RtParticipation() {
  }

  public RtParticipationKey getId() {
    return this.id;
  }

  public void setId(RtParticipationKey id) {
    this.id = id;
  }

  public Long getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getStatusUid() {
    return this.statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public Long getSummaryTime() {
    return this.summaryTime;
  }

  public void setSummaryTime(Long summaryTime) {
    this.summaryTime = summaryTime;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtEntry getRtEntry() {
    return this.rtEntry;
  }

  public void setRtEntry(RtEntry rtEntry) {
    this.rtEntry = rtEntry;
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

  public RtEventStartblock getRtEventStartblock() {
    return this.rtEventStartblock;
  }

  public void setRtEventStartblock(RtEventStartblock rtEventStartblock) {
    this.rtEventStartblock = rtEventStartblock;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtParticipation(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtParticipation(null);

    return rtRace;
  }

  public void setStartblockUid(Long startblockUid) {
    this.startblockUid = startblockUid;
  }

  public void setClassUid(Long classUid) {
    this.classUid = classUid;
  }

}
