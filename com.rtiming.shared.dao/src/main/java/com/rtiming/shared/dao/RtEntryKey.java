package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_entry database table.
 */
@Embeddable
public class RtEntryKey extends AbstractKey<RtEntryKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "entry_nr")
  private Long entryNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtEntryKey() {
  }

  @Override
  public Long getId() {
    return this.entryNr;
  }

  @Override
  public void setId(Long entryNr) {
    this.entryNr = entryNr;
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
    if (!(other instanceof RtEntryKey)) {
      return false;
    }
    RtEntryKey castOther = (RtEntryKey) other;
    return this.entryNr.equals(castOther.entryNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.entryNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtEntryKey create(RtEntryKey key) {
    if (key == null) {
      key = new RtEntryKey();
    }
    return (RtEntryKey) createKeyInternal(key);
  }

  public static RtEntryKey create(Long id) {
    RtEntryKey key = new RtEntryKey();
    key.setId(id);
    return create(key);
  }

}
