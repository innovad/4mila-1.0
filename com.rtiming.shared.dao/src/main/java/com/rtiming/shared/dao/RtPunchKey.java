package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_punch database table.
 */
@Embeddable
public class RtPunchKey extends AbstractKey<RtPunchKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  private Long sortcode;

  @Column(name = "punch_session_nr")
  private Long punchSessionNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtPunchKey() {
  }

  public Long getSortcode() {
    return this.sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
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
    if (!(other instanceof RtPunchKey)) {
      return false;
    }
    RtPunchKey castOther = (RtPunchKey) other;
    return this.sortcode.equals(castOther.sortcode)
        && this.punchSessionNr.equals(castOther.punchSessionNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.sortcode.hashCode();
    hash = hash * prime + this.punchSessionNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtPunchKey create(RtPunchKey key) {
    if (key == null) {
      key = new RtPunchKey();
    }
    return (RtPunchKey) createKeyInternal(key);
  }

  public static RtPunchKey create(Long id) {
    RtPunchKey key = new RtPunchKey();
    key.setId(id);
    return create(key);
  }

}
