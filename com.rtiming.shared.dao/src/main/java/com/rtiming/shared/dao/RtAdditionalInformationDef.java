package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the rt_additional_information_def database table.
 */
@Entity
@Table(name = "rt_additional_information_def")
public class RtAdditionalInformationDef implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtAdditionalInformationDefKey id;

  @Column(name = "default_decimal")
  private Double defaultDecimal;

  @Column(name = "default_integer")
  private Long defaultInteger;

  @Column(name = "default_text")
  private String defaultText;

  @Column(name = "entity_uid")
  private Long entityUid;

  private Boolean mandatory;

  @Column(name = "parent_uid")
  private Long parentUid;

  @Column(name = "type_uid")
  private Long typeUid;

  @Column(name = "fee_group_nr")
  private Long feeGroupNr;

  @Column(name = "value_max")
  private Double valueMax;

  @Column(name = "value_min")
  private Double valueMin;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtFeeGroup
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "fee_group_nr", referencedColumnName = "fee_group_nr", insertable = false, updatable = false)
  })
  private RtFeeGroup rtFeeGroup;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "additional_information_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc;

  public RtAdditionalInformationDef() {
  }

  public RtAdditionalInformationDefKey getId() {
    return this.id;
  }

  public void setId(RtAdditionalInformationDefKey id) {
    this.id = id;
  }

  public Double getDefaultDecimal() {
    return this.defaultDecimal;
  }

  public void setDefaultDecimal(Double defaultDecimal) {
    this.defaultDecimal = defaultDecimal;
  }

  public Long getDefaultLong() {
    return this.defaultInteger;
  }

  public void setDefaultLong(Long defaultLong) {
    this.defaultInteger = defaultLong;
  }

  public String getDefaultText() {
    return this.defaultText;
  }

  public void setDefaultText(String defaultText) {
    this.defaultText = defaultText;
  }

  public Long getEntityUid() {
    return this.entityUid;
  }

  public void setEntityUid(Long entityUid) {
    this.entityUid = entityUid;
  }

  public Boolean getMandatory() {
    return this.mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Long getParentUid() {
    return this.parentUid;
  }

  public void setParentUid(Long parentUid) {
    this.parentUid = parentUid;
  }

  public Long getTypeUid() {
    return this.typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Double getValueMax() {
    return this.valueMax;
  }

  public void setValueMax(Double valueMax) {
    this.valueMax = valueMax;
  }

  public Double getValueMin() {
    return this.valueMin;
  }

  public void setValueMin(Double valueMin) {
    this.valueMin = valueMin;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtFeeGroup getRtFeeGroup() {
    return this.rtFeeGroup;
  }

  public void setRtFeeGroup(RtFeeGroup rtFeeGroup) {
    this.rtFeeGroup = rtFeeGroup;
  }

  public Long getFeeGroupNr() {
    return feeGroupNr;
  }

  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

}
