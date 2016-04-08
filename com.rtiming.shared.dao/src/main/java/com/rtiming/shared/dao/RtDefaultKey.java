package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_default database table.
 */
@Embeddable
public class RtDefaultKey extends AbstractKey<RtDefaultKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "default_uid")
  private Long defaultUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtDefaultKey() {
  }

  @Override
  public Long getId() {
    return this.defaultUid;
  }

  @Override
  public void setId(Long defaultUid) {
    this.defaultUid = defaultUid;
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
    if (!(other instanceof RtDefaultKey)) {
      return false;
    }
    RtDefaultKey castOther = (RtDefaultKey) other;
    return this.defaultUid.equals(castOther.defaultUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.defaultUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtDefaultKey create(RtDefaultKey key) {
    if (key == null) {
      key = new RtDefaultKey();
    }
    return (RtDefaultKey) createKeyInternal(key);
  }

  public static RtDefaultKey create(Long id) {
    RtDefaultKey key = new RtDefaultKey();
    key.setId(id);
    return create(key);
  }

}
