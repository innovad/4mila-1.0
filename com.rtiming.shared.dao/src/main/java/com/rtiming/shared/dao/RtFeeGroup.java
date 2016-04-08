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

/**
 * The persistent class for the rt_fee_group database table.
 */
@Entity
@Table(name = "rt_fee_group")
public class RtFeeGroup implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtFeeGroupKey id;

  @Column(name = "cash_payment_on_registration")
  private Boolean cashPaymentOnRegistration;

  private String name;

  //bi-directional many-to-one association to RtAdditionalInformationDef
  @OneToMany(mappedBy = "rtFeeGroup")
  private List<RtAdditionalInformationDef> rtAdditionalInformationDefs;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtFeeGroup")
  private List<RtEventClass> rtEventClasses;

  //bi-directional many-to-one association to RtFee
  @OneToMany(mappedBy = "rtFeeGroup")
  private List<RtFee> rtFees;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  public RtFeeGroup() {
  }

  public RtFeeGroupKey getId() {
    return this.id;
  }

  public void setId(RtFeeGroupKey id) {
    this.id = id;
  }

  public Boolean getCashPaymentOnRegistration() {
    return this.cashPaymentOnRegistration;
  }

  public void setCashPaymentOnRegistration(Boolean cashPaymentOnRegistration) {
    this.cashPaymentOnRegistration = cashPaymentOnRegistration;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<RtAdditionalInformationDef> getRtAdditionalInformationDefs() {
    return this.rtAdditionalInformationDefs;
  }

  public void setRtAdditionalInformationDefs(List<RtAdditionalInformationDef> rtAdditionalInformationDefs) {
    this.rtAdditionalInformationDefs = rtAdditionalInformationDefs;
  }

  public RtAdditionalInformationDef addRtAdditionalInformationDef(RtAdditionalInformationDef rtAdditionalInformationDef) {
    getRtAdditionalInformationDefs().add(rtAdditionalInformationDef);
    rtAdditionalInformationDef.setRtFeeGroup(this);

    return rtAdditionalInformationDef;
  }

  public RtAdditionalInformationDef removeRtAdditionalInformationDef(RtAdditionalInformationDef rtAdditionalInformationDef) {
    getRtAdditionalInformationDefs().remove(rtAdditionalInformationDef);
    rtAdditionalInformationDef.setRtFeeGroup(null);

    return rtAdditionalInformationDef;
  }

  public List<RtEventClass> getRtEventClasses() {
    return this.rtEventClasses;
  }

  public void setRtEventClasses(List<RtEventClass> rtEventClasses) {
    this.rtEventClasses = rtEventClasses;
  }

  public RtEventClass addRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().add(rtEventClass);
    rtEventClass.setRtFeeGroup(this);

    return rtEventClass;
  }

  public RtEventClass removeRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().remove(rtEventClass);
    rtEventClass.setRtFeeGroup(null);

    return rtEventClass;
  }

  public List<RtFee> getRtFees() {
    return this.rtFees;
  }

  public void setRtFees(List<RtFee> rtFees) {
    this.rtFees = rtFees;
  }

  public RtFee addRtFee(RtFee rtFee) {
    getRtFees().add(rtFee);
    rtFee.setRtFeeGroup(this);

    return rtFee;
  }

  public RtFee removeRtFee(RtFee rtFee) {
    getRtFees().remove(rtFee);
    rtFee.setRtFeeGroup(null);

    return rtFee;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

}
