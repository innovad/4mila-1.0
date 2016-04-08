package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_map database table.
 */
@Embeddable
public class RtMapKey extends AbstractKey<RtMapKey> implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "map_nr")
  private Long mapNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtMapKey() {
  }

  @Override
  public Long getId() {
    return this.mapNr;
  }

  @Override
  public void setId(Long mapNr) {
    this.mapNr = mapNr;
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
    if (!(other instanceof RtMapKey)) {
      return false;
    }
    RtMapKey castOther = (RtMapKey) other;
    return this.mapNr.equals(castOther.mapNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.mapNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtMapKey create(RtMapKey key) {
    if (key == null) {
      key = new RtMapKey();
    }
    return (RtMapKey) createKeyInternal(key);
  }

  public static RtMapKey create(Long id) {
    RtMapKey key = new RtMapKey();
    key.setId(id);
    return create(key);
  }

}
