package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_event database table.
 */
@Embeddable
public class RtEventKey extends AbstractKey<RtEventKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEventKey() {
  }

  @Override
  public Long getId() {
    return this.eventNr;
  }

  @Override
  public void setId(Long eventNr) {
    this.eventNr = eventNr;
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
    if (!(other instanceof RtEventKey)) {
      return false;
    }
    RtEventKey castOther = (RtEventKey) other;
    return this.eventNr.equals(castOther.eventNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.eventNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtEventKey create(RtEventKey key) {
    if (key == null) {
      key = new RtEventKey();
    }
    return (RtEventKey) createKeyInternal(key);
  }

  public static RtEventKey create(Long id) {
    RtEventKey key = new RtEventKey();
    key.setId(id);
    return create(key);
  }

}
