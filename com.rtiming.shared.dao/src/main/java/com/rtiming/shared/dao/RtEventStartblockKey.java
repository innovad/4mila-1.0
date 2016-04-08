package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_event_startblock database table.
 */
@Embeddable
public class RtEventStartblockKey extends AbstractKey<RtEventStartblockKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "startblock_uid")
  private Long startblockUid;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEventStartblockKey() {
  }

  @Override
  public Long getId() {
    return this.startblockUid;
  }

  @Override
  public void setId(Long startblockUid) {
    this.startblockUid = startblockUid;
  }

  public Long getEventNr() {
    return this.eventNr;
  }

  public void setEventNr(Long eventNr) {
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
    if (!(other instanceof RtEventStartblockKey)) {
      return false;
    }
    RtEventStartblockKey castOther = (RtEventStartblockKey) other;
    return this.startblockUid.equals(castOther.startblockUid)
        && this.eventNr.equals(castOther.eventNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.startblockUid.hashCode();
    hash = hash * prime + this.eventNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtEventStartblockKey create(RtEventStartblockKey key) {
    if (key == null) {
      key = new RtEventStartblockKey();
    }
    return (RtEventStartblockKey) createKeyInternal(key);
  }

  public static RtEventStartblockKey create(Long id) {
    RtEventStartblockKey key = new RtEventStartblockKey();
    key.setId(id);
    return create(key);
  }

}
