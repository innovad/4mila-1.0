package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the rt_default database table.
 */
@Entity
@Table(name = "rt_default")
public class RtDefault implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtDefaultKey id;

  @Column(name = "value_integer")
  private Long valueInteger;

  @Column(name = "value_string")
  private String valueString;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  public RtDefault() {
  }

  public RtDefaultKey getId() {
    return this.id;
  }

  public void setId(RtDefaultKey id) {
    this.id = id;
  }

  public Long getValueLong() {
    return this.valueInteger;
  }

  public void setValueLong(Long valueInteger) {
    this.valueInteger = valueInteger;
  }

  public String getValueString() {
    return this.valueString;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

}
