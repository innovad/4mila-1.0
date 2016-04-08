package com.rtiming.shared.dao;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The persistent class for the rt_entry database table.
 */
@Entity
@Table(name = "rt_entry")
@UploadConfiguration(uploadOrder = 510, filteredColumns = {"registration_nr"})
public class RtEntry implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEntryKey id;

  @Column(name = "currency_uid")
  private Long currencyUid;

  @Column(name = "evt_entry")
  private Timestamp evtEntry;

  @Column(name = "status_uid")
  private Long statusUid;

  @Column(name = "registration_nr")
  private Long registrationNr;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtRegistration
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "registration_nr", referencedColumnName = "registration_nr", insertable = false, updatable = false)
  })
  private RtRegistration rtRegistration;

  //bi-directional many-to-one association to RtParticipation
  @OneToMany(mappedBy = "rtEntry")
  private List<RtParticipation> rtParticipations;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtEntry")
  private List<RtRace> rtRaces;

  public RtEntry() {
  }

  public RtEntryKey getId() {
    return this.id;
  }

  public void setId(RtEntryKey id) {
    this.id = id;
  }

  public Long getCurrencyUid() {
    return this.currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public Timestamp getEvtEntry() {
    return this.evtEntry;
  }

  public void setEvtEntry(Timestamp evtEntry) {
    this.evtEntry = evtEntry;
  }

  public Long getStatusUid() {
    return this.statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtRegistration getRtRegistration() {
    return this.rtRegistration;
  }

  public void setRtRegistration(RtRegistration rtRegistration) {
    this.rtRegistration = rtRegistration;
  }

  public List<RtParticipation> getRtParticipations() {
    return this.rtParticipations;
  }

  public void setRtParticipations(List<RtParticipation> rtParticipations) {
    this.rtParticipations = rtParticipations;
  }

  public RtParticipation addRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().add(rtParticipation);
    rtParticipation.setRtEntry(this);

    return rtParticipation;
  }

  public RtParticipation removeRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().remove(rtParticipation);
    rtParticipation.setRtEntry(null);

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
    rtRace.setRtEntry(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtEntry(null);

    return rtRace;
  }

}
