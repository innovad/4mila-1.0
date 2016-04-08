package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_class_age database table.
 */
@Embeddable
public class RtClassAgeKey extends AbstractKey<RtClassAgeKey> implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "class_age_nr")
  private Long classAgeNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtClassAgeKey() {
  }

  @Override
  public Long getId() {
    return this.classAgeNr;
  }

  @Override
  public void setId(Long classAgeNr) {
    this.classAgeNr = classAgeNr;
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
    if (!(other instanceof RtClassAgeKey)) {
      return false;
    }
    RtClassAgeKey castOther = (RtClassAgeKey) other;
    return this.classAgeNr.equals(castOther.classAgeNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.classAgeNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtClassAgeKey create(RtClassAgeKey key) {
    if (key == null) {
      key = new RtClassAgeKey();
    }
    return (RtClassAgeKey) createKeyInternal(key);
  }

  public static RtClassAgeKey create(Long id) {
    RtClassAgeKey key = new RtClassAgeKey();
    key.setId(id);
    return create(key);
  }

}
