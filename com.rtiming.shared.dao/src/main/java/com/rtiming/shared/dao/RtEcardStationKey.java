package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_event database table.
 */
@Embeddable
public class RtEcardStationKey extends AbstractKey<RtEcardStationKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "station_nr")
  private Long stationNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEcardStationKey() {
  }

  @Override
  public Long getId() {
    return this.stationNr;
  }

  @Override
  public void setId(Long eventNr) {
    this.stationNr = eventNr;
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
    if (!(other instanceof RtEcardStationKey)) {
      return false;
    }
    RtEcardStationKey castOther = (RtEcardStationKey) other;
    return this.stationNr.equals(castOther.stationNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.stationNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtEcardStationKey create(RtEcardStationKey key) {
    if (key == null) {
      key = new RtEcardStationKey();
    }
    return (RtEcardStationKey) createKeyInternal(key);
  }

  public static RtEcardStationKey create(Long id) {
    RtEcardStationKey key = new RtEcardStationKey();
    key.setId(id);
    return create(key);
  }

}
