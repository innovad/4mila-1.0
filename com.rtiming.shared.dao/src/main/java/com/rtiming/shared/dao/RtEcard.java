package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_ecard database table.
 */
@Entity
@Table(name = "rt_ecard")
@UploadConfiguration(uploadOrder = 250)
public class RtEcard implements Serializable {
  private static final long serialVersionUID = 1L;

  public RtEcard() {
  }

  @EmbeddedId
  private RtEcardKey id;

  @Column(name = "ecard_no")
  @Index(name = "rt_ecard_ix_ecard_no")
  private String ecardNo;

  @Column(name = "rental_card")
  private Boolean rentalCard;

  @Column(name = "type_uid")
  private Long typeUid;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtPunchSession
  @OneToMany(mappedBy = "rtEcard")
  private List<RtPunchSession> rtPunchSessions;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtEcard")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtRunner
  @OneToMany(mappedBy = "rtEcard")
  private List<RtRunner> rtRunners;

  public RtEcardKey getKey() {
    return this.id;
  }

  public void setKey(RtEcardKey key) {
    this.id = key;
  }

  public String getEcardNo() {
    return this.ecardNo;
  }

  public void setEcardNo(String ecardNo) {
    this.ecardNo = ecardNo;
  }

  public Boolean getRentalCard() {
    return this.rentalCard;
  }

  public void setRentalCard(Boolean rentalCard) {
    this.rentalCard = rentalCard;
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

  public List<RtPunchSession> getRtPunchSessions() {
    return this.rtPunchSessions;
  }

  public void setRtPunchSessions(List<RtPunchSession> rtPunchSessions) {
    this.rtPunchSessions = rtPunchSessions;
  }

  public RtPunchSession addRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().add(rtPunchSession);
    rtPunchSession.setRtEcard(this);

    return rtPunchSession;
  }

  public RtPunchSession removeRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().remove(rtPunchSession);
    rtPunchSession.setRtEcard(null);

    return rtPunchSession;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtEcard(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtEcard(null);

    return rtRace;
  }

  public List<RtRunner> getRtRunners() {
    return this.rtRunners;
  }

  public void setRtRunners(List<RtRunner> rtRunners) {
    this.rtRunners = rtRunners;
  }

  public RtRunner addRtRunner(RtRunner rtRunner) {
    getRtRunners().add(rtRunner);
    rtRunner.setRtEcard(this);

    return rtRunner;
  }

  public RtRunner removeRtRunner(RtRunner rtRunner) {
    getRtRunners().remove(rtRunner);
    rtRunner.setRtEcard(null);

    return rtRunner;
  }

}
