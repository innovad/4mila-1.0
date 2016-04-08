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
 * The persistent class for the rt_payment database table.
 */
@Entity
@Table(name = "rt_payment")
public class RtPayment implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtPaymentKey id;

  private double amount;

  @Column(name = "currency_uid")
  private Long currencyUid;

  @Column(name = "evt_payment")
  private Timestamp evtPayment;

  @Column(name = "payment_no")
  private String paymentNo;

  @Column(name = "type_uid")
  private Long typeUid;

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

  //bi-directional many-to-one association to RtCurrency
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "currency_uid", referencedColumnName = "currency_uid", insertable = false, updatable = false)
  })
  private RtCurrency rtCurrency;

  public RtPayment() {
  }

  public RtPaymentKey getId() {
    return this.id;
  }

  public void setId(RtPaymentKey id) {
    this.id = id;
  }

  public double getAmount() {
    return this.amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Long getCurrencyUid() {
    return this.currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public Timestamp getEvtPayment() {
    return this.evtPayment;
  }

  public void setEvtPayment(Timestamp evtPayment) {
    this.evtPayment = evtPayment;
  }

  public String getPaymentNo() {
    return this.paymentNo;
  }

  public void setPaymentNo(String paymentNo) {
    this.paymentNo = paymentNo;
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

  public RtRegistration getRtRegistration() {
    return this.rtRegistration;
  }

  public void setRtRegistration(RtRegistration rtRegistration) {
    this.rtRegistration = rtRegistration;
  }

}
