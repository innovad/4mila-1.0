package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_race database table.
 */
@Embeddable
public class RtRaceKey extends AbstractKey<RtRaceKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "race_nr")
  private Long raceNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtRaceKey() {
  }

  @Override
  public Long getId() {
    return this.raceNr;
  }

  @Override
  public void setId(Long raceNr) {
    this.raceNr = raceNr;
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
    if (!(other instanceof RtRaceKey)) {
      return false;
    }
    RtRaceKey castOther = (RtRaceKey) other;
    return this.raceNr.equals(castOther.raceNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.raceNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtRaceKey create(RtRaceKey key) {
    if (key == null) {
      key = new RtRaceKey();
    }
    return (RtRaceKey) createKeyInternal(key);
  }

  public static RtRaceKey create(Long id) {
    RtRaceKey key = new RtRaceKey();
    key.setId(id);
    return create(key);
  }

}
