package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_club database table.
 */
@Embeddable
public class RtClubKey extends AbstractKey<RtClubKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "club_nr")
  private Long clubNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtClubKey() {
  }

  @Override
  public Long getId() {
    return this.clubNr;
  }

  @Override
  public void setId(Long clubNr) {
    this.clubNr = clubNr;
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
    if (!(other instanceof RtClubKey)) {
      return false;
    }
    RtClubKey castOther = (RtClubKey) other;
    return this.clubNr.equals(castOther.clubNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.clubNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtClubKey create(RtClubKey key) {
    if (key == null) {
      key = new RtClubKey();
    }
    return (RtClubKey) createKeyInternal(key);
  }

  public static RtClubKey create(Long id) {
    RtClubKey key = new RtClubKey();
    key.setId(id);
    return create(key);
  }

}
