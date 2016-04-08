package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_ecard database table.
 */
@Embeddable
public class RtEcardKey extends AbstractKey<RtEcardKey> implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "ecard_nr")
  private Long eCardNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEcardKey() {
  }

  @Override
  public Long getId() {
    return this.eCardNr;
  }

  @Override
  public void setId(Long eCardNr) {
    this.eCardNr = eCardNr;
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
    if (!(other instanceof RtEcardKey)) {
      return false;
    }
    RtEcardKey castOther = (RtEcardKey) other;
    return this.eCardNr.equals(castOther.eCardNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.eCardNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtEcardKey create(RtEcardKey key) {
    if (key == null) {
      key = new RtEcardKey();
    }
    return (RtEcardKey) createKeyInternal(key);
  }

  public static RtEcardKey create(Long id) {
    RtEcardKey key = new RtEcardKey();
    key.setId(id);
    return create(key);
  }

}
