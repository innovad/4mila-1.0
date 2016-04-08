package com.rtiming.shared.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the rt_fee database table.
 */
@Entity
@Table(name = "rt_fee")
public class RtFee implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtFeeKey id;

  @Column(name = "age_from")
  private Long ageFrom;

  @Column(name = "age_to")
  private Long ageTo;

  @Column(name = "currency_uid")
  private Long currencyUid;

  @Column(name = "evt_from")
  private Timestamp evtFrom;

  @Column(name = "evt_to")
  private Timestamp evtTo;

  @Column(name = "fee_group_nr")
  private Long feeGroupNr;

  private double fee;

  //bi-directional many-to-one association to RtFeeGroup
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "fee_group_nr", referencedColumnName = "fee_group_nr", insertable = false, updatable = false)
  })
  private RtFeeGroup rtFeeGroup;

  //bi-directional many-to-one association to RtFeeGroup
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "currency_uid", referencedColumnName = "currency_uid", insertable = false, updatable = false)
  })
  private RtCurrency rtCurrency;

  public RtFee() {
  }

  public RtFeeKey getId() {
    return this.id;
  }

  public void setId(RtFeeKey id) {
    this.id = id;
  }

  public Long getAgeFrom() {
    return this.ageFrom;
  }

  public void setAgeFrom(Long ageFrom) {
    this.ageFrom = ageFrom;
  }

  public Long getAgeTo() {
    return this.ageTo;
  }

  public void setAgeTo(Long ageTo) {
    this.ageTo = ageTo;
  }

  public Long getCurrencyUid() {
    return this.currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public Timestamp getEvtFrom() {
    return this.evtFrom;
  }

  public void setEvtFrom(Timestamp evtFrom) {
    this.evtFrom = evtFrom;
  }

  public Timestamp getEvtTo() {
    return this.evtTo;
  }

  public void setEvtTo(Timestamp evtTo) {
    this.evtTo = evtTo;
  }

  public double getFee() {
    return this.fee;
  }

  public void setFee(double fee) {
    this.fee = fee;
  }

  public RtFeeGroup getRtFeeGroup() {
    return this.rtFeeGroup;
  }

  public void setRtFeeGroup(RtFeeGroup rtFeeGroup) {
    this.rtFeeGroup = rtFeeGroup;
  }

  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

}
