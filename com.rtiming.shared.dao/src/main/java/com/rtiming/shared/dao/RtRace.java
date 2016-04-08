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
 * The persistent class for the rt_race database table.
 */
@Entity
@Table(name = "rt_race")
@UploadConfiguration(uploadOrder = 530)
public class RtRace implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRaceKey id;

  @Column(name = "bib_no")
  private String bibNo;

  @Column(name = "nation_uid")
  private Long nationUid;

  @Column(name = "leg_start_time")
  private Long legStartTime;

  @Column(name = "leg_time")
  private Long legTime;

  @Column(name = "manual_status")
  private Boolean manualStatus;

  @Column(name = "status_uid")
  private Long statusUid;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "ecard_nr")
  private Long eCardNr;

  @Column(name = "entry_nr")
  private Long entryNr;

  @Column(name = "runner_nr")
  private Long runnerNr;

  @Column(name = "club_nr")
  private Long clubNr;

  @Column(name = "leg_class_uid")
  private Long legClassUid;

  @Column(name = "address_nr")
  private Long addressNr;

  //bi-directional many-to-one association to RtPosition
  @OneToMany(mappedBy = "rtRace")
  private List<RtPosition> rtPositions;

  //bi-directional many-to-one association to RtPunchSession
  @OneToMany(mappedBy = "rtRace")
  private List<RtPunchSession> rtPunchSessions;

  //bi-directional many-to-one association to RtAddress
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "address_nr", referencedColumnName = "address_nr", insertable = false, updatable = false),
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false)
  })
  private RtAddress rtAddress;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtClub
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "club_nr", referencedColumnName = "club_nr", insertable = false, updatable = false)
  })
  private RtClub rtClub;

  //bi-directional many-to-one association to RtEcard
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "ecard_nr", referencedColumnName = "ecard_nr", insertable = false, updatable = false)
  })
  private RtEcard rtEcard;

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
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false),
      @JoinColumn(name = "leg_class_uid", referencedColumnName = "class_uid", insertable = false, updatable = false)
  })
  private RtEventClass rtEventClass;

  //bi-directional many-to-one association to RtParticipation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "entry_nr", referencedColumnName = "entry_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtParticipation rtParticipation;

  //bi-directional many-to-one association to RtRunner
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "runner_nr", referencedColumnName = "runner_nr", insertable = false, updatable = false)
  })
  private RtRunner rtRunner;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "nation_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "leg_class_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtLegClassUc;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "nation_uid", referencedColumnName = "country_uid", insertable = false, updatable = false)
  })
  private RtCountry rtCountry;

  //bi-directional many-to-one association to RtRaceControl
  @OneToMany(mappedBy = "rtRace")
  private List<RtRaceControl> rtRaceControls;

  public RtRace() {
  }

  public RtRaceKey getId() {
    return this.id;
  }

  public void setId(RtRaceKey id) {
    this.id = id;
  }

  public String getBibNo() {
    return this.bibNo;
  }

  public void setBibNo(String bibNo) {
    this.bibNo = bibNo;
  }

  public Long getLegStartTime() {
    return this.legStartTime;
  }

  public void setLegStartTime(Long legStartTime) {
    this.legStartTime = legStartTime;
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

  public Long getStatusUid() {
    return this.statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public List<RtPosition> getRtPositions() {
    return this.rtPositions;
  }

  public void setRtPositions(List<RtPosition> rtPositions) {
    this.rtPositions = rtPositions;
  }

  public RtPosition addRtPosition(RtPosition rtPosition) {
    getRtPositions().add(rtPosition);
    rtPosition.setRtRace(this);

    return rtPosition;
  }

  public RtPosition removeRtPosition(RtPosition rtPosition) {
    getRtPositions().remove(rtPosition);
    rtPosition.setRtRace(null);

    return rtPosition;
  }

  public List<RtPunchSession> getRtPunchSessions() {
    return this.rtPunchSessions;
  }

  public void setRtPunchSessions(List<RtPunchSession> rtPunchSessions) {
    this.rtPunchSessions = rtPunchSessions;
  }

  public RtPunchSession addRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().add(rtPunchSession);
    rtPunchSession.setRtRace(this);

    return rtPunchSession;
  }

  public RtPunchSession removeRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().remove(rtPunchSession);
    rtPunchSession.setRtRace(null);

    return rtPunchSession;
  }

  public RtAddress getRtAddress() {
    return this.rtAddress;
  }

  public void setRtAddress(RtAddress rtAddress) {
    this.rtAddress = rtAddress;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtClub getRtClub() {
    return this.rtClub;
  }

  public void setRtClub(RtClub rtClub) {
    this.rtClub = rtClub;
  }

  public RtEcard getRtEcard() {
    return this.rtEcard;
  }

  public void setRtEcard(RtEcard rtEcard) {
    this.rtEcard = rtEcard;
  }

  public RtEntry getRtEntry() {
    return this.rtEntry;
  }

  public void setRtEntry(RtEntry rtEntry) {
    this.rtEntry = rtEntry;
  }

  public RtEventClass getRtEventClass() {
    return this.rtEventClass;
  }

  public void setRtEventClass(RtEventClass rtEventClass) {
    this.rtEventClass = rtEventClass;
  }

  public RtParticipation getRtParticipation() {
    return this.rtParticipation;
  }

  public void setRtParticipation(RtParticipation rtParticipation) {
    this.rtParticipation = rtParticipation;
  }

  public RtRunner getRtRunner() {
    return this.rtRunner;
  }

  public void setRtRunner(RtRunner rtRunner) {
    this.rtRunner = rtRunner;
  }

  public RtUc getRtUc() {
    return this.rtUc;
  }

  public void setRtUc(RtUc rtUc) {
    this.rtUc = rtUc;
  }

  public List<RtRaceControl> getRtRaceControls() {
    return this.rtRaceControls;
  }

  public void setRtRaceControls(List<RtRaceControl> rtRaceControls) {
    this.rtRaceControls = rtRaceControls;
  }

  public RtRaceControl addRtRaceControl(RtRaceControl rtRaceControl) {
    getRtRaceControls().add(rtRaceControl);
    rtRaceControl.setRtRace(this);

    return rtRaceControl;
  }

  public RtRaceControl removeRtRaceControl(RtRaceControl rtRaceControl) {
    getRtRaceControls().remove(rtRaceControl);
    rtRaceControl.setRtRace(null);

    return rtRaceControl;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public void setEcardNr(Long ecardNr) {
    this.eCardNr = ecardNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public void setLegClassUid(Long legClassUid) {
    this.legClassUid = legClassUid;
  }

}
