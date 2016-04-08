package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
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
 * The persistent class for the rt_punch_session database table.
 */
@Entity
@Table(name = "rt_punch_session")
public class RtPunchSession implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtPunchSessionKey id;

  @Column(name = "ecard_check")
  private Long ecardCheck;

  @Column(name = "ecard_clear")
  private Long ecardClear;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "evt_download")
  private Date evtDownload;

  private Long finish;

  @Column(name = "raw_data", columnDefinition = "clob")
  private String rawData;

  @Column(name = "race_nr")
  private Long raceNr;

  @Column(name = "ecard_nr")
  private Long eCardNr;

  @Column(name = "station_nr")
  private Long stationNr;

  private Long start;

  //bi-directional many-to-one association to RtPunch
  @OneToMany(mappedBy = "rtPunchSession")
  private List<RtPunch> rtPunches;

  //bi-directional many-to-one association to RtEcard
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "ecard_nr", referencedColumnName = "ecard_nr", insertable = false, updatable = false)
  })
  private RtEcard rtEcard;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtEcardStation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "station_nr", referencedColumnName = "station_nr", insertable = false, updatable = false)
  })
  private RtEcardStation rtEcardStation;

  //bi-directional many-to-one association to RtRace
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "race_nr", referencedColumnName = "race_nr", insertable = false, updatable = false)
  })
  private RtRace rtRace;

  public RtPunchSession() {
  }

  public RtPunchSessionKey getId() {
    return this.id;
  }

  public void setId(RtPunchSessionKey id) {
    this.id = id;
  }

  public Long getEcardCheck() {
    return this.ecardCheck;
  }

  public void setEcardCheck(Long ecardCheck) {
    this.ecardCheck = ecardCheck;
  }

  public Long getEcardClear() {
    return this.ecardClear;
  }

  public void setEcardClear(Long ecardClear) {
    this.ecardClear = ecardClear;
  }

  public Long getEventNr() {
    return this.eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Date getEvtDownload() {
    return this.evtDownload;
  }

  public void setEvtDownload(Date evtDownload) {
    this.evtDownload = evtDownload;
  }

  public Long getFinish() {
    return this.finish;
  }

  public void setFinish(Long finish) {
    this.finish = finish;
  }

  public String getRawData() {
    return this.rawData;
  }

  public void setRawData(String rawData) {
    this.rawData = rawData;
  }

  public Long getStart() {
    return this.start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  public List<RtPunch> getRtPunches() {
    return this.rtPunches;
  }

  public void setRtPunches(List<RtPunch> rtPunches) {
    this.rtPunches = rtPunches;
  }

  public RtPunch addRtPunch(RtPunch rtPunch) {
    getRtPunches().add(rtPunch);
    rtPunch.setRtPunchSession(this);

    return rtPunch;
  }

  public RtPunch removeRtPunch(RtPunch rtPunch) {
    getRtPunches().remove(rtPunch);
    rtPunch.setRtPunchSession(null);

    return rtPunch;
  }

  public RtEcard getRtEcard() {
    return this.rtEcard;
  }

  public void setRtEcard(RtEcard rtEcard) {
    this.rtEcard = rtEcard;
  }

  public RtEcardStation getRtEcardStation() {
    return this.rtEcardStation;
  }

  public void setRtEcardStation(RtEcardStation rtEcardStation) {
    this.rtEcardStation = rtEcardStation;
  }

  public RtRace getRtRace() {
    return this.rtRace;
  }

  public void setRtRace(RtRace rtRace) {
    this.rtRace = rtRace;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long geteCardNr() {
    return eCardNr;
  }

}
