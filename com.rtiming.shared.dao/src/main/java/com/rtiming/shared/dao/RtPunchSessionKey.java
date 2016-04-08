package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_punch_session database table.
 */
@Embeddable
public class RtPunchSessionKey extends AbstractKey<RtPunchSessionKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "punch_session_nr")
  private Long punchSessionNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtPunchSessionKey() {
  }

  @Override
  public Long getId() {
    return this.punchSessionNr;
  }

  @Override
  public void setId(Long punchSessionNr) {
    this.punchSessionNr = punchSessionNr;
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
    if (!(other instanceof RtPunchSessionKey)) {
      return false;
    }
    RtPunchSessionKey castOther = (RtPunchSessionKey) other;
    return this.punchSessionNr.equals(castOther.punchSessionNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.punchSessionNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtPunchSessionKey create(RtPunchSessionKey key) {
    if (key == null) {
      key = new RtPunchSessionKey();
    }
    return (RtPunchSessionKey) createKeyInternal(key);
  }

  public static RtPunchSessionKey create(Long id) {
    RtPunchSessionKey key = new RtPunchSessionKey();
    key.setId(id);
    return create(key);
  }

}
