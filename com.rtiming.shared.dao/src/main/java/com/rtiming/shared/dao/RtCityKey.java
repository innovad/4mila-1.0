package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_city database table.
 */
@Embeddable
public class RtCityKey extends AbstractKey<RtCityKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "city_nr")
  private Long cityNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtCityKey() {
  }

  @Override
  public Long getId() {
    return this.cityNr;
  }

  @Override
  public void setId(Long cityNr) {
    this.cityNr = cityNr;
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
    if (!(other instanceof RtCityKey)) {
      return false;
    }
    RtCityKey castOther = (RtCityKey) other;
    return this.cityNr.equals(castOther.cityNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.cityNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtCityKey create(RtCityKey key) {
    if (key == null) {
      key = new RtCityKey();
    }
    return (RtCityKey) createKeyInternal(key);
  }

  public static RtCityKey create(Long id) {
    RtCityKey key = new RtCityKey();
    key.setId(id);
    return create(key);
  }

}
