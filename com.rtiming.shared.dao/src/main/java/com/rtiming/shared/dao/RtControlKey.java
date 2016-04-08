package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_club database table.
 */
@Embeddable
public class RtControlKey extends AbstractKey<RtControlKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "control_nr")
  private Long controlNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtControlKey() {
  }

  @Override
  public Long getId() {
    return this.controlNr;
  }

  @Override
  public void setId(Long clubNr) {
    this.controlNr = clubNr;
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
    if (!(other instanceof RtControlKey)) {
      return false;
    }
    RtControlKey castOther = (RtControlKey) other;
    return this.controlNr.equals(castOther.controlNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.controlNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtControlKey create(RtControlKey key) {
    if (key == null) {
      key = new RtControlKey();
    }
    return (RtControlKey) createKeyInternal(key);
  }

  public static RtControlKey create(Long id) {
    RtControlKey key = new RtControlKey();
    key.setId(id);
    return create(key);
  }

}
