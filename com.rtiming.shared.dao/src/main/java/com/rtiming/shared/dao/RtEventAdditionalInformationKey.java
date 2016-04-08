package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_event_class database table.
 */
@Embeddable
public class RtEventAdditionalInformationKey implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "event_nr")
  private Long eventNr;

  @Column(name = "additional_information_uid")
  private Long additionalInformationUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEventAdditionalInformationKey() {
  }

  public Long getEventNr() {
    return this.eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getAdditionalInformationUid() {
    return additionalInformationUid;
  }

  public void setAdditionalInformationUid(Long additionalInformationUid) {
    this.additionalInformationUid = additionalInformationUid;
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
    if (!(other instanceof RtEventAdditionalInformationKey)) {
      return false;
    }
    RtEventAdditionalInformationKey castOther = (RtEventAdditionalInformationKey) other;
    return this.eventNr.equals(castOther.eventNr)
        && this.additionalInformationUid.equals(castOther.additionalInformationUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.eventNr.hashCode();
    hash = hash * prime + this.additionalInformationUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }
}
