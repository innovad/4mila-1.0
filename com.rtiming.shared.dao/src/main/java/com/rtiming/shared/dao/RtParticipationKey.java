package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_participation database table.
 */
@Embeddable
public class RtParticipationKey implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "entry_nr")
  private Long entryNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtParticipationKey() {
  }

  public Long getEventNr() {
    return this.eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getEntryNr() {
    return this.entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getClientNr() {
    return this.clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RtParticipationKey)) {
      return false;
    }
    RtParticipationKey castOther = (RtParticipationKey) other;
    return this.eventNr.equals(castOther.eventNr)
        && this.entryNr.equals(castOther.entryNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.eventNr.hashCode();
    hash = hash * prime + this.entryNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }
}
