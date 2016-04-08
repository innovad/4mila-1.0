package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_registration database table.
 */
@Embeddable
public class RtRegistrationKey extends AbstractKey<RtRegistrationKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "registration_nr")
  private Long registrationNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtRegistrationKey() {
  }

  @Override
  public Long getId() {
    return this.registrationNr;
  }

  @Override
  public void setId(Long registrationNr) {
    this.registrationNr = registrationNr;
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
    if (!(other instanceof RtRegistrationKey)) {
      return false;
    }
    RtRegistrationKey castOther = (RtRegistrationKey) other;
    return this.registrationNr.equals(castOther.registrationNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.registrationNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtRegistrationKey create(RtRegistrationKey key) {
    if (key == null) {
      key = new RtRegistrationKey();
    }
    return (RtRegistrationKey) createKeyInternal(key);
  }

  public static RtRegistrationKey create(Long id) {
    RtRegistrationKey key = new RtRegistrationKey();
    key.setId(id);
    return create(key);
  }

}
