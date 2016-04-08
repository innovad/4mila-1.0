package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
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

/**
 * The persistent class for the rt_registration database table.
 */
@Entity
@Table(name = "rt_registration")
public class RtRegistration implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRegistrationKey id;

  @Column(name = "evt_registration")
  private Date evtRegistration;

  @Column(name = "registration_no")
  @Index(name = "rt_registration_ix_registration_no")
  private String registrationNo;

  @Column(name = "startlist_setting_option_uid")
  private Long startlistSettingOptionUid;

  @Column(name = "total_fee")
  private double totalFee;

  //bi-directional many-to-one association to RtEntry
  @OneToMany(mappedBy = "rtRegistration")
  private List<RtEntry> rtEntries;

  //bi-directional many-to-one association to RtPayment
  @OneToMany(mappedBy = "rtRegistration")
  private List<RtPayment> rtPayments;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  public RtRegistration() {
  }

  public RtRegistrationKey getId() {
    return this.id;
  }

  public void setId(RtRegistrationKey id) {
    this.id = id;
  }

  public Date getEvtRegistration() {
    return this.evtRegistration;
  }

  public void setEvtRegistration(Date evtRegistration) {
    this.evtRegistration = evtRegistration;
  }

  public String getRegistrationNo() {
    return this.registrationNo;
  }

  public void setRegistrationNo(String registrationNo) {
    this.registrationNo = registrationNo;
  }

  public Long getStartlistSettingOptionUid() {
    return this.startlistSettingOptionUid;
  }

  public void setStartlistSettingOptionUid(Long startlistSettingOptionUid) {
    this.startlistSettingOptionUid = startlistSettingOptionUid;
  }

  public double getTotalFee() {
    return this.totalFee;
  }

  public void setTotalFee(double totalFee) {
    this.totalFee = totalFee;
  }

  public List<RtEntry> getRtEntries() {
    return this.rtEntries;
  }

  public void setRtEntries(List<RtEntry> rtEntries) {
    this.rtEntries = rtEntries;
  }

  public RtEntry addRtEntry(RtEntry rtEntry) {
    getRtEntries().add(rtEntry);
    rtEntry.setRtRegistration(this);

    return rtEntry;
  }

  public RtEntry removeRtEntry(RtEntry rtEntry) {
    getRtEntries().remove(rtEntry);
    rtEntry.setRtRegistration(null);

    return rtEntry;
  }

  public List<RtPayment> getRtPayments() {
    return this.rtPayments;
  }

  public void setRtPayments(List<RtPayment> rtPayments) {
    this.rtPayments = rtPayments;
  }

  public RtPayment addRtPayment(RtPayment rtPayment) {
    getRtPayments().add(rtPayment);
    rtPayment.setRtRegistration(this);

    return rtPayment;
  }

  public RtPayment removeRtPayment(RtPayment rtPayment) {
    getRtPayments().remove(rtPayment);
    rtPayment.setRtRegistration(null);

    return rtPayment;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

}
