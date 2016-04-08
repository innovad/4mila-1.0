package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_race database table.
 */
@Embeddable
public class RtRaceControlKey extends AbstractKey<RtRaceControlKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "race_control_nr")
  private Long raceControlNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtRaceControlKey() {
  }

  @Override
  public Long getId() {
    return this.raceControlNr;
  }

  @Override
  public void setId(Long raceControlNr) {
    this.raceControlNr = raceControlNr;
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
    if (!(other instanceof RtRaceControlKey)) {
      return false;
    }
    RtRaceControlKey castOther = (RtRaceControlKey) other;
    return this.raceControlNr.equals(castOther.raceControlNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.raceControlNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtRaceControlKey create(RtRaceControlKey key) {
    if (key == null) {
      key = new RtRaceControlKey();
    }
    return (RtRaceControlKey) createKeyInternal(key);
  }

  public static RtRaceControlKey create(Long id) {
    RtRaceControlKey key = new RtRaceControlKey();
    key.setId(id);
    return create(key);
  }

}
