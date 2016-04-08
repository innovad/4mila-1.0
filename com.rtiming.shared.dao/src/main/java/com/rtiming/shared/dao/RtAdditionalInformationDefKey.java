package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_additional_information_def database table.
 */
@Embeddable
public class RtAdditionalInformationDefKey extends AbstractKey<RtAdditionalInformationDefKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "additional_information_uid")
  private Long additionalInformationUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtAdditionalInformationDefKey() {
  }

  @Override
  public Long getId() {
    return this.additionalInformationUid;
  }

  @Override
  public void setId(Long additionalInformationUid) {
    this.additionalInformationUid = additionalInformationUid;
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
    if (!(other instanceof RtAdditionalInformationDefKey)) {
      return false;
    }
    RtAdditionalInformationDefKey castOther = (RtAdditionalInformationDefKey) other;
    return this.additionalInformationUid.equals(castOther.additionalInformationUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.additionalInformationUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtAdditionalInformationDefKey create(RtAdditionalInformationDefKey key) {
    if (key == null) {
      key = new RtAdditionalInformationDefKey();
    }
    return (RtAdditionalInformationDefKey) createKeyInternal(key);
  }

  public static RtAdditionalInformationDefKey create(Long id) {
    RtAdditionalInformationDefKey key = new RtAdditionalInformationDefKey();
    key.setId(id);
    return create(key);
  }

}
