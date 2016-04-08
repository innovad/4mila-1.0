package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_country database table.
 */
@Embeddable
public class RtCountryKey extends AbstractKey<RtCountryKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "country_uid")
  private Long countryUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtCountryKey() {
  }

  @Override
  public Long getId() {
    return this.countryUid;
  }

  @Override
  public void setId(Long countryUid) {
    this.countryUid = countryUid;
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
    if (!(other instanceof RtCountryKey)) {
      return false;
    }
    RtCountryKey castOther = (RtCountryKey) other;
    return this.countryUid.equals(castOther.countryUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.countryUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtCountryKey create(RtCountryKey key) {
    if (key == null) {
      key = new RtCountryKey();
    }
    return (RtCountryKey) createKeyInternal(key);
  }

  public static RtCountryKey create(Long id) {
    RtCountryKey key = new RtCountryKey();
    key.setId(id);
    return create(key);
  }

}
