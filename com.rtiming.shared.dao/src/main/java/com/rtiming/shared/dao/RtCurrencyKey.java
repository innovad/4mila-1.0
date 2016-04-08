package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_currency database table.
 */
@Embeddable
public class RtCurrencyKey extends AbstractKey<RtCurrencyKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "currency_uid")
  private Long currencyUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtCurrencyKey() {
  }

  @Override
  public Long getId() {
    return this.currencyUid;
  }

  @Override
  public void setId(Long currencyUid) {
    this.currencyUid = currencyUid;
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
    if (!(other instanceof RtCurrencyKey)) {
      return false;
    }
    RtCurrencyKey castOther = (RtCurrencyKey) other;
    return this.currencyUid.equals(castOther.currencyUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.currencyUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtCurrencyKey create(RtCurrencyKey key) {
    if (key == null) {
      key = new RtCurrencyKey();
    }
    return (RtCurrencyKey) createKeyInternal(key);
  }

  public static RtCurrencyKey create(Long id) {
    RtCurrencyKey key = new RtCurrencyKey();
    key.setId(id);
    return create(key);
  }

}
