package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_user database table.
 */
@Embeddable
public class RtUserKey extends AbstractKey<RtUserKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "user_nr")
  private Long userNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtUserKey() {
  }

  @Override
  public Long getId() {
    return this.userNr;
  }

  @Override
  public void setId(Long userNr) {
    this.userNr = userNr;
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
    if (!(other instanceof RtUserKey)) {
      return false;
    }
    RtUserKey castOther = (RtUserKey) other;
    return this.userNr.equals(castOther.userNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.userNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtUserKey create(RtUserKey key) {
    if (key == null) {
      key = new RtUserKey();
    }
    return (RtUserKey) createKeyInternal(key);
  }

  public static RtUserKey create(Long id) {
    RtUserKey key = new RtUserKey();
    key.setId(id);
    return create(key);
  }

}
