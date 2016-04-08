package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_fee_group database table.
 */
@Embeddable
public class RtFeeGroupKey extends AbstractKey<RtFeeGroupKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "fee_group_nr")
  private Long feeGroupNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtFeeGroupKey() {
  }

  @Override
  public Long getId() {
    return this.feeGroupNr;
  }

  @Override
  public void setId(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

  @Override
  public Long getClientNr() {
    return this.clientNr;
  }

  @Override
  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RtFeeGroupKey)) {
      return false;
    }
    RtFeeGroupKey castOther = (RtFeeGroupKey) other;
    return this.feeGroupNr.equals(castOther.feeGroupNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.feeGroupNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtFeeGroupKey create(RtFeeGroupKey key) {
    if (key == null) {
      key = new RtFeeGroupKey();
    }
    return (RtFeeGroupKey) createKeyInternal(key);
  }

  public static RtFeeGroupKey create(Long id) {
    RtFeeGroupKey key = new RtFeeGroupKey();
    key.setId(id);
    return create(key);
  }

}
