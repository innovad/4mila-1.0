package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_uc database table.
 */
@Embeddable
public class RtUcKey extends AbstractKey<RtUcKey> implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "uc_uid")
  private Long ucUid;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtUcKey() {
  }

  @Override
  public Long getId() {
    return this.ucUid;
  }

  @Override
  public void setId(Long ucUid) {
    this.ucUid = ucUid;
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
    if (!(other instanceof RtUcKey)) {
      return false;
    }
    RtUcKey castOther = (RtUcKey) other;
    return this.ucUid.equals(castOther.ucUid)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.ucUid.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtUcKey create(RtUcKey key) {
    if (key == null) {
      key = new RtUcKey();
    }
    return (RtUcKey) createKeyInternal(key);
  }

  public static RtUcKey create(Long id) {
    RtUcKey key = new RtUcKey();
    key.setId(id);
    return create(key);
  }

}
