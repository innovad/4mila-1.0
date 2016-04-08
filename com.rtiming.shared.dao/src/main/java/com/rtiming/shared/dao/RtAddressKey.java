package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_address database table.
 */
@Embeddable
public class RtAddressKey extends AbstractKey<RtAddressKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "address_nr")
  private Long addressNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtAddressKey() {
  }

  @Override
  public Long getId() {
    return this.addressNr;
  }

  @Override
  public void setId(Long addressNr) {
    this.addressNr = addressNr;
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
    if (!(other instanceof RtAddressKey)) {
      return false;
    }
    RtAddressKey castOther = (RtAddressKey) other;
    return this.addressNr.equals(castOther.addressNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.addressNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtAddressKey create(RtAddressKey key) {
    if (key == null) {
      key = new RtAddressKey();
    }
    return (RtAddressKey) createKeyInternal(key);
  }

  public static RtAddressKey create(Long id) {
    RtAddressKey key = new RtAddressKey();
    key.setId(id);
    return create(key);
  }

}
