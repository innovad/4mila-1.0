package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_fee database table.
 */
@Embeddable
public class RtFeeKey extends AbstractKey<RtFeeKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "fee_nr")
  private Long feeNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtFeeKey() {
  }

  @Override
  public Long getId() {
    return this.feeNr;
  }

  @Override
  public void setId(Long feeNr) {
    this.feeNr = feeNr;
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
    if (!(other instanceof RtFeeKey)) {
      return false;
    }
    RtFeeKey castOther = (RtFeeKey) other;
    return this.feeNr.equals(castOther.feeNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.feeNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtFeeKey create(RtFeeKey key) {
    if (key == null) {
      key = new RtFeeKey();
    }
    return (RtFeeKey) createKeyInternal(key);
  }

  public static RtFeeKey create(Long id) {
    RtFeeKey key = new RtFeeKey();
    key.setId(id);
    return create(key);
  }

}
